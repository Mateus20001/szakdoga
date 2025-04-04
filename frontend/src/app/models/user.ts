export interface User {
    id:string;
    password:string;
}
export interface UserShowDTO{
    identifier: string;
    firstName: string;
    lastName: string;
    name: string;
    emails: string[];
    phoneNumbers: string[];
}