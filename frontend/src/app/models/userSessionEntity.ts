// models/user-session.entity.ts


export class UserSessionEntity {
    constructor(
        public identifier: string,
        public roles: Role[]
    ) {}
}
export enum Role {
    STUDENT = 'STUDENT',
    ADMIN = 'ADMIN',
    TEACHER = 'TEACHER',
  }