import { Component } from '@angular/core';
import { GlobalSettingsService } from '../services/global-settings.service';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { CommonModule } from '@angular/common';
export interface GlobalSettings {
  id: number;
  name: string;
  activated: boolean;
  attribute: string;
}
@Component({
  selector: 'app-global-settings-changer',
  standalone: true,
  imports: [FormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
    MatButtonModule,
    CommonModule],
  templateUrl: './global-settings-changer.component.html',
  styleUrl: './global-settings-changer.component.scss'
})
export class GlobalSettingsChangerComponent {
  settings: GlobalSettings[] = [];
  editSetting: GlobalSettings | null = null;

  constructor(private settingsService: GlobalSettingsService) {}

  ngOnInit(): void {
    this.loadSettings();
  }

  loadSettings(): void {
    this.settingsService.getSettings().subscribe(data => {
      this.settings = data;
    });
  }

  startEdit(setting: GlobalSettings): void {
    this.editSetting = { ...setting }; // make a copy to edit
  }

  saveEdit(): void {
    if (this.editSetting) {
      this.settingsService.updateSetting(this.editSetting).subscribe(() => {
        this.editSetting = null;
        this.loadSettings();
      });
    }
  }

  cancelEdit(): void {
    this.editSetting = null;
  }
}
