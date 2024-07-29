interface InputFieldsProps {
  formData: any;
  handleInputChange: (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => void;
  selectedOption: string;
}

const InputFields: React.FC<InputFieldsProps> = ({ formData, handleInputChange, selectedOption }) => {
  const fields = selectedOption === 'dosage'
    ? ['name', 'volume', 'num', 'times', 'storageInfo', 'details']
    : ['reason', 'details'];

  return (
    <>
      {fields.map((label, index) => (
        <div className="mb-4" key={index}>
          <p className="text-base font-medium text-left text-[#353c4e] mb-2">
            {label === 'name' ? '약의 종류' :
              label === 'volume' ? '투약 용량' :
                label === 'num' ? '투약 횟수' :
                  label === 'times' ? '투약 시간 (쉼표로 구분)' :
                    label === 'storageInfo' ? '보관 방법' :
                      label === 'details' ? '특이 사항' :
                        label === 'reason' ? '사유' : '기타 사항'}
          </p>
          {label === 'details' || label === 'details' ? (
            <textarea name={label} className="w-full p-2 border rounded" onChange={handleInputChange}></textarea>
          ) : (
            <input type="text" name={label} className="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-[#FDDA6E]" onChange={handleInputChange} />
          )}
        </div>
      ))}
    </>
  );
};

export default InputFields;