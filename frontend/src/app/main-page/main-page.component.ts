import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { MatColumnDef, MatTable, MatTableDataSource } from '@angular/material/table';
import { MatTableModule } from '@angular/material/table';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MessageDto } from '../models/email';
import { Quote } from '../models/Quote';
import { QuoteService } from '../services/quoteservice';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [MatTableModule,MatSortModule,MatPaginatorModule,MatButtonModule,MatIconModule, CommonModule],
  templateUrl: './main-page.component.html',
  styleUrl: './main-page.component.scss',
  providers: [AuthService]
})
export class MainPageComponent implements AfterViewInit {
  messages: MatTableDataSource<MessageDto> = new MatTableDataSource<MessageDto>();
  quote?: Quote;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  displayedColumns: string[] = ['from', 'message', 'dateTime'];

  constructor(private userMessageService: AuthService, private quoteService: QuoteService) {}

  ngAfterViewInit() {
    this.messages.sort = this.sort;
    this.messages.paginator = this.paginator;
  }

  ngOnInit(): void {
    this.quoteService.getDailyQuote().subscribe(data => {
      this.quote = data[0];
    });
    this.loadMessages();
  }

  loadMessages(): void {
    this.userMessageService.getUserMessages().subscribe(
      (data: MessageDto[]) => {
        console.log(data);
        this.messages.data = data;
      },
      (error) => {
        console.error('Error fetching messages:', error);
      }
    );
  }
  formatDate(isoString: string): string {
    const date = new Date(isoString);
    return date.toLocaleString('hu-HU', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
  
}
