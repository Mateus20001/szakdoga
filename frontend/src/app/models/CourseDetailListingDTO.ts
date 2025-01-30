export interface CourseDetailListingDTO {
    id: number,
    name: string;
    description: string;
    credits: number;
}
export interface CourseDetailDTO {
  name: string;
  description: string;
  credits: number;
  recommendedHalfYear: number;
  requirementType: string;
  requiredCourses: number[];
  enrollmentTypes: EnrollmentTypeDTO[];
  teachers: string[];
}
export interface EnrollmentTypeDTO {
    majorId: number;
    enrollmentType: string;
}
export interface CourseDetailStudentListingDTO {
  id: number;
  name: string;
  credits: number;
  requirementType: string;
  enrollmentType: string;
  recommendedHalfYear: number;
  course_dates_size: number;
}