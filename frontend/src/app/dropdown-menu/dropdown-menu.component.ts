import { NgFor, NgIf } from '@angular/common';
import { Component, Input } from '@angular/core';
import { menuDropdownItem } from '../menuDropdownItem';
import { RouterModule } from '@angular/router';
@Component({
  selector: 'app-dropdown-menu',
  standalone: true,
  imports: [NgFor, NgIf, RouterModule],
  templateUrl: './dropdown-menu.component.html',
  styleUrl: './dropdown-menu.component.scss'
})
export class DropdownMenuComponent {

  
  @Input() menuName!: string;
  @Input() menuDropdownList!: menuDropdownItem[];
}
