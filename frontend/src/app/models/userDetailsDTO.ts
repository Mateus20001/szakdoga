export class UserDetailsDTO {
    id: string;
    firstName: string;
    lastName: string;
    name: string;
    roles: string[];
    emails: string[];
    majors: string[];
    faculties: string[];
    phone_numbers: string[];
    createdAt: string;
    updatedAt: string;
  
    constructor(
      id: string,
      firstName: string,
      lastName: string,
      name: string,
      roles: string[],
      emails: string[],
      majors: string[],
      faculties: string[],
      phone_numbers: string[],
      createdAt: string,
      updatedAt: string
    ) {
      this.id = id;
      this.firstName = firstName;
      this.lastName = lastName;
      this.name = name;
      this.roles = roles;
      this.emails = emails;
      this.majors = majors;
      this.faculties = faculties;
      this.phone_numbers = phone_numbers;
      this.createdAt = createdAt;
      this.updatedAt = updatedAt;
    }
  }
  