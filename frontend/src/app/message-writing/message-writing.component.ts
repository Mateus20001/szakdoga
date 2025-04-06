import { CommonModule } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule, MatIconButton } from '@angular/material/button';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MessageService } from '../services/message.service';
import { MatAutocompleteModule, MatOption } from '@angular/material/autocomplete';
import { UserListingDTO } from '../models/TeacherStudentGradingDTO';

@Component({
  selector: 'app-message-writing',
  standalone: true,
  imports: [ReactiveFormsModule,
      MatInputModule,
      MatButtonModule,
      MatFormFieldModule,
      CommonModule,
      FormsModule,
      MatAutocompleteModule,
      MatOption],
  templateUrl: './message-writing.component.html',
  styleUrl: './message-writing.component.scss'
})
export class MessageWritingComponent {
  messageText: string = '';
  receiverId: string = '';
  allUsers: any[] = [];
  messageService: MessageService
  filteredUsers: UserListingDTO[] = [];
  constructor(@Inject(MAT_DIALOG_DATA) public data: { teacherId: string, users: any[], messageService: MessageService }) {
    // Set the receiver ID based on the teacherId passed into the dialog
    if (data.teacherId) {
      this.receiverId = data.teacherId;
    }
    this.allUsers = data.users;
    this.messageService = data.messageService;
  }
  ngOnInit() {
  }
  

  sendMessage() {
    const message = {
      text: this.messageText,
      to: this.receiverId
    };

    // Call the addMessage method from the service
    this.messageService.addMessage(message).subscribe(
      response => {
        console.log('Message sent successfully', response);
        
      },
      error => {
        console.error('Error sending message', error);
      }
    );
  }
  onReceiverIdSelected(event: any) {
    this.receiverId = event.option.value;
  }
  onInputChange(event: any) {
    const value = event.target.value.toLowerCase();

    // Filter the list of users based on the input value (case-insensitive)
    this.filteredUsers = this.allUsers.filter(user =>
      user.id.toLowerCase().includes(value) ||
      user.firstName.toLowerCase().includes(value) ||
      user.lastName.toLowerCase().includes(value)
    );
  }
}
