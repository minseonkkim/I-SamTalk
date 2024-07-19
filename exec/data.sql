-- Kindergarten 테이블에 초기 데이터 삽입
INSERT INTO kindergarten (kindergarten_name) VALUES ('사랑 유치원');
INSERT INTO kindergarten (kindergarten_name) VALUES ('희망 유치원');
INSERT INTO kindergarten (kindergarten_name) VALUES ('기쁨 유치원');

-- KindergartenClass 테이블에 초기 데이터 삽입
INSERT INTO kindergarten_class (kindergarten_id, kindergarten_class_name)
VALUES 
    ((SELECT kindergarten_id FROM kindergarten WHERE kindergarten_name = '사랑 유치원'), '사랑반'),
    ((SELECT kindergarten_id FROM kindergarten WHERE kindergarten_name = '사랑 유치원'), '기쁨반'),
    ((SELECT kindergarten_id FROM kindergarten WHERE kindergarten_name = '희망 유치원'), '꿈나무반'),
    ((SELECT kindergarten_id FROM kindergarten WHERE kindergarten_name = '희망 유치원'), '햇살반'),
    ((SELECT kindergarten_id FROM kindergarten WHERE kindergarten_name = '기쁨 유치원'), '별빛반'),
    ((SELECT kindergarten_id FROM kindergarten WHERE kindergarten_name = '기쁨 유치원'), '달빛반');