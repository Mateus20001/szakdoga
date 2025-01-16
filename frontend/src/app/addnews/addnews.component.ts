import { NgClass, NgIf } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, NgModule } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormField, MatLabel } from '@angular/material/form-field';
import { MatInput, MatInputModule } from '@angular/material/input';
import { NewsService } from '../services/news.service';

@Component({
  selector: 'app-addnews',
  standalone: true,
  imports: [MatFormField, MatLabel, MatInputModule, NgIf, ReactiveFormsModule, NgClass],
  templateUrl: './addnews.component.html',
  styleUrl: './addnews.component.scss'
})
export class AddnewsComponent {
  form: FormGroup;
  selectedFile: File | null = null;
  filePreview: string | null = null;
  message: string = '';
  messageType: string = '';

  constructor(private fb: FormBuilder, private newsService: NewsService) {
    this.form = this.fb.group({
      title: ['', Validators.required],
      shortDesc: ['', Validators.required],
      longDesc: ['', Validators.required],
    });
  }

  onFileSelect(event: any): void {
    const file: File = event.target.files[0];
    console.log(this.form)
    if (file) {
      this.selectedFile = file;
      if (file.type.startsWith('image')) {
        const reader = new FileReader();
        reader.onload = (e: any) => {
          this.filePreview = e.target.result; 
        };
        reader.readAsDataURL(file);
      }
    }
  }

  onSubmit(): void {
    if (this.form.invalid || !this.selectedFile) {
      return;
    }

    const formData = new FormData();
    formData.append('file', this.selectedFile, this.selectedFile.name);
    formData.append('title', this.form.get('title')?.value);
    formData.append('shortDesc', this.form.get('shortDesc')?.value);
    formData.append('longDesc', this.form.get('longDesc')?.value);

    this.newsService.addNews(formData).subscribe(
      (response: any) => {
        this.message = 'News created successfully!';
        this.messageType = 'success';
      },
      (error: any) => {
        this.message = 'Error submitting form: ' + error.message;
        this.messageType = 'error';
      }
    );
  }
}
