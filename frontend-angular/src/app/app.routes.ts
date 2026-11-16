import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'productos', pathMatch: 'full' },
  {
    path: 'productos',
    loadComponent: () => import('./components/productos-page/productos-page.component').then(m => m.ProductosPageComponent),
    title: 'Catálogo de Productos - tcsw-ventas'
  },
  {
    path: 'productos/nuevo',
    loadComponent: () => import('./components/producto-form/producto-form.component').then(m => m.ProductoFormComponent),
    title: 'Registrar Producto - tcsw-ventas'
  },
  {
    path: '**',
    redirectTo: 'productos'
  }
];

