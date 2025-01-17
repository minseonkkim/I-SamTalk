let ws: WebSocket | null = null;
let intervalId: number | null = null;

export const startWebSocket = (url: string, kindergartenId: number, selectedOption: string) => {
  const wsUrl = `${url}/${kindergartenId}`;
  ws = new WebSocket(wsUrl);

  ws.onopen = () => {
    // 서버로 인증 토큰과 kindergartenId, selectedOption 전송
    ws.send(JSON.stringify({ type: 'authenticate', kindergartenId: kindergartenId, selectedOption: selectedOption }));

    if (navigator.geolocation) {
      const sendLocation = () => {
        navigator.geolocation.getCurrentPosition(
          (position) => {
            const data = {
              type: 'location',
              latitude: position.coords.latitude,
              longitude: position.coords.longitude,
              kindergartenId,
              selectedOption // selectedOption 포함
            };
            if (ws && ws.readyState === WebSocket.OPEN) {
              ws.send(JSON.stringify(data));
            }
          },
          (error) => {
            console.error('Geolocation error:', error);
          },
          {
            enableHighAccuracy: true,
            timeout: 20000,
            maximumAge: 0
          }
        );
      };
      sendLocation();
      intervalId = window.setInterval(sendLocation, 500);
    }
  };

  ws.onclose = () => {
    if (intervalId) {
      clearInterval(intervalId);
    }
    ws = null;
  };

  ws.onerror = (error) => {
    console.error('WebSocket error:', error);
  };
};

export const stopWebSocket = () => {
  if (ws) {
    ws.send(JSON.stringify({ type: 'disconnect' }));
    ws.close();
  }
  if (intervalId) {
    clearInterval(intervalId);
  }
};



export function receiveBusLocation(wsRef, setLocation, mapRef, busMarkerRef, setIsMoving, busCenterFlag, setBusOption) {
  const ws = wsRef.current;
  if (!ws) {
    console.error('WebSocket is not initialized');
    return;
  }
  let count = 0;
  let lastCenter = null;
  busCenterFlag.current = false;
  ws.onmessage = (event) => {
    const data = JSON.parse(event.data);
    if (data.type === 'disconnect') {
      busCenterFlag.current = false
      if (busMarkerRef.current && lastCenter) {
        busMarkerRef.current.setPosition(lastCenter);
      }

      setIsMoving(false);
    } else {
      setLocation({ lat: data.latitude, lng: data.longitude });
      setIsMoving(true);
      setBusOption(data.selectedOption); // 버스 옵션 설정

      const newCenter = new window.kakao.maps.LatLng(data.latitude, data.longitude);
      lastCenter = newCenter;
      const map = mapRef.current;
      if (map && !busCenterFlag.current && data.latitude !== undefined && data.longitude !== undefined) {
        map.setCenter(newCenter)
        busCenterFlag.current = true
      }
      if (busMarkerRef.current) {
        busMarkerRef.current.setPosition(newCenter);
      }
    }
  };

  ws.onclose = () => {
    setIsMoving(false);
  };

  ws.onerror = (error) => {
    console.error('WebSocket error:', error);
    setIsMoving(false);
  };

  window.addEventListener('beforeunload', () => {
    if (wsRef.current) {
      wsRef.current.close();
    }
  });

  return () => {
    if (wsRef.current) {
      wsRef.current.close();
    }
  };
}
