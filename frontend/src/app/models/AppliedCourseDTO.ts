import { Grade } from "./Grade";

export interface AppliedCourse {
    courseDetailName: string;
    courseDateId: number;
    courseDateName: string;
    teacherIds: string[];
    location: string;
    grades: Grade[];
  }
  export interface StudentStatistic {
    
    bestGradeValue: number;
    creationDate: string;
  }
