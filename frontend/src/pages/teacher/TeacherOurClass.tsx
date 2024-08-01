import NavigateBack from "../../components/teacher/common/NavigateBack";
import TeacherHeader from "../../components/teacher/common/TeacherHeader";
import Title from "../../components/teacher/common/Title";
import ChildCard from "../../components/teacher/ourclass/ChildCard";
import ProfileImg from "../../assets/teacher/profile_img.jpg";
import { useEffect, useState } from "react";
import { useTeacherInfoStore } from "../../stores/useTeacherInfoStore";
import { getClassChilds } from "../../api/kindergarten";
import { getDocumentsByDate } from "../../api/document";

interface Absent{
    absentId: number;
}

interface Dosage{
    dosageId: number;
}

interface ChildDocument {
    absentExists: boolean;
    dosageExists: boolean;
    absents: Absent[];
    dosages: Dosage[];
}

export default function TeacherOurClass() {
    const [childs, setChilds] = useState([]);
    const [absentCount, setAbsentCount] = useState(0);
    const [dosageCount, setDosageCount] = useState(0);

    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');

    const calculateAge = (birthDateString: string): number => {
        const birthdate = new Date(birthDateString);
        const today = new Date();
        let age = today.getFullYear() - birthdate.getFullYear();
        const monthDifference = today.getMonth() - birthdate.getMonth();

        if (monthDifference < 0 || (monthDifference === 0 && today.getDate() < birthdate.getDate())) {
            age--;
        }

        return age;
    };

    const checkDocumentExists = async (childId: number): Promise<ChildDocument> => {
        const documents = await getDocumentsByDate(childId, year + '-' + month + '-' + day);
        return documents;
    }

    useEffect(() => {
        const classId = useTeacherInfoStore.getState().teacherInfo.kindergartenClassId;
        const fetchChilds = async () => {
            try {
                const fetchedChilds = await getClassChilds(classId);

                const childPromises = fetchedChilds.map(async (child: any) => {
                    const documentStatus = await checkDocumentExists(child.childId);
                    
                    const absentDocumentIds = documentStatus.absentExists ? documentStatus.absents.map((absent: any) => absent.absentId) : [];
                    const dosageDocumentIds = documentStatus.dosageExists ? documentStatus.dosages.map((dosage: any) => dosage.dosageId) : [];
                
                    return {
                        ...child,
                        age: calculateAge(child.birth),
                        absentExists: documentStatus.absentExists,
                        dosageExists: documentStatus.dosageExists,
                        absentDocumentIds: documentStatus.absentExists ? absentDocumentIds : [],
                        dosageDocumentIds: documentStatus.dosageExists ? dosageDocumentIds : []
                    };
                });

                const childsWithAgeAndStatus = await Promise.all(childPromises);
                setChilds(childsWithAgeAndStatus);

                const absentCount = childsWithAgeAndStatus.filter(child => child.absentExists).length;
                const dosageCount = childsWithAgeAndStatus.filter(child => child.dosageExists).length;

                setAbsentCount(absentCount);
                setDosageCount(dosageCount);
            } catch (error) {
                console.error("Failed to fetch childs:", error);
            }
        };

        fetchChilds();
    }, []);

    return (
        <>
            <TeacherHeader />
            <div className="mt-[85px] px-[150px] flex flex-col items-center">            
                <NavigateBack backPage="홈" backLink='/' />            
                <Title title={useTeacherInfoStore.getState().teacherInfo.kindergartenClassName} />
                <div className="absolute top-[125px] right-[400px] bg-[#ffdfdf] px-5 py-2 font-bold rounded-[10px] flex flex-row items-center text-xl font-bold">
                    결석
                </div>
                <span className="absolute top-[125px] right-[350px] px-3 py-2 flex flex-row items-center text-xl font-bold">{absentCount}명</span>
                <div className="absolute top-[125px] right-[250px] bg-[#e7dfff] px-5 py-2 font-bold rounded-[10px] flex flex-row items-center text-xl font-bold">
                    투약
                </div>
                <span className="absolute top-[125px] right-[200px] px-3 py-2 flex flex-row items-center text-xl font-bold">{dosageCount}명</span>
                <div className="grid gap-4 w-full" style={{gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))'}}>
                    {childs.map((child, index) => (
                        <ChildCard 
                            key={index}
                            name={child.name} 
                            gender={child.gender} 
                            age={child.age} 
                            absent={child.absentExists} 
                            dosage={child.dosageExists} 
                            absentId = {child.absentDocumentIds}
                            dosageId = {child.dosageDocumentIds}
                            profileImgPath={child.profile}
                        />
                    ))}
                </div>
            </div>
        </>
    );
}
