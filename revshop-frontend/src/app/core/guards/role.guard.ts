import { CanActivateFn } from '@angular/router';

export const roleGuard: CanActivateFn = (route) => {

  const role = localStorage.getItem('role');
  const expectedRole = route.data?.['role'];

  return role === expectedRole;
};
