import { Routes } from '@angular/router';
import { LoginComponent } from './login.component';
import { ContractorListComponent } from './contractor-list.component';
import { ContractorDetailComponent } from './contractor-detail.component';
import { AuthGuard } from './auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'contractors/:id', component: ContractorDetailComponent, canActivate: [AuthGuard] },
  { path: '', component: ContractorListComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: '' }
];
