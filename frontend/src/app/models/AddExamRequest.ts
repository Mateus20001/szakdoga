export interface AddExamRequest {
  examDate: string;
  location: string; 
  longevity: number;
  type: 'ONLINE' | 'ATTENDANCE';
  courseDateId: number;
}
