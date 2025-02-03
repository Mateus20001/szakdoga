export interface UserListingDTO {
    identifier: string;
    firstName: string;
    lastName: string;
    name: string;
  }
  
export interface GradeDTO {
    id: number;
    gradeValue: number;
    gradedBy: string;
    creationDate: string;
}

export interface TeacherStudentGradingDTO {
    courseDetailName: string;
    courseDateId: number;
    courseDateName: string;
    students: UserListingDTO[];
    grades: GradeDTO[];
}