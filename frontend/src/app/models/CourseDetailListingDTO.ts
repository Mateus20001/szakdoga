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