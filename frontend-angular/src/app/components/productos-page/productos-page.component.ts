import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ProductosService } from '../../services/productos.service';
import { Producto } from '../../models/producto.model';
import { EstadoUi } from '../../models/ui-state.model';
import { ProductoListComponent } from '../producto-list/producto-list.component';

@Component({
  selector: 'app-productos-page',
  standalone: true,
  imports: [CommonModule, RouterLink, ProductoListComponent],
  templateUrl: './productos-page.component.html',
  styleUrls: ['./productos-page.component.css']
})
export class ProductosPageComponent implements OnInit {
  private readonly productosService = inject(ProductosService);

  productos: Producto[] = [];
  estadoUi: EstadoUi = { tipo: 'idle' };

  ngOnInit(): void {
    this.cargarProductos();
  }

  cargarProductos(): void {
    this.estadoUi = { tipo: 'loading' };

    this.productosService.obtenerTodos().subscribe({
      next: (datos) => {
        this.productos = datos;
        if (datos.length === 0) {
          this.estadoUi = { tipo: 'empty' };
        } else {
          this.estadoUi = { tipo: 'success' };
        }
      },
      error: (err: Error) => {
        this.estadoUi = {
          tipo: 'error',
          mensajeError: err.message
        };
      }
    });
  }
}
