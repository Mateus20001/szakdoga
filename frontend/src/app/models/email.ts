export interface Email {
    id: number;
    emailAddress: string; 
  }

  export interface Contact {
    id: number;
    email: string;
    _public: boolean;
  }
  export interface MessageDto {
    text: string;
    from: string;
    dateTime: string;
  }