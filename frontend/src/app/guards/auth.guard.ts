import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

// The new function-based guard approach
export const authGuard: CanActivateFn = () => {
  const router = inject(Router);
  if (localStorage.getItem('loggedInUser')) {
    return true;
  } else {
    router.navigate(['/login']);
    return false;
  }
};
