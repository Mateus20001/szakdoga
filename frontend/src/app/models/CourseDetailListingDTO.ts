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
  requirementType: string;
  requiredCourses: number[];
  enrollmentTypes: EnrollmentTypeDTO[];
}
export interface EnrollmentTypeDTO {
    majorId: number;
    enrollmentType: string;
}