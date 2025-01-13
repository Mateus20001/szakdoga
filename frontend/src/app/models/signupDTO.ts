export class SignupDTO {
    firstName: string;
    lastName: string;
    password: string;
    name: string;
  
    constructor(firstName: string, lastName: string, password: string, name: string) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.password = password;
      this.name = name;
    }
  }