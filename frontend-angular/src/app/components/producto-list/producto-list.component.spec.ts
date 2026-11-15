import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProductoListComponent } from './producto-list.component';
import { Producto } from '../../models/producto.model';

describe('ProductoListComponent', () => {
  let component: ProductoListComponent;
  let fixture: ComponentFixture<ProductoListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductoListComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(ProductoListComponent);
    component = fixture.componentInstance;
  });

  it('debe crear el componente de lista', () => {
    expect(component).toBeTruthy();
  });

  it('debe renderizar la lista de productos correctamente en la tabla', () => {
    const mockProductos: Producto[] = [
      { id: 1, nombre: 'Teclado Mecánico', precio: 850.0, existencia: 12 },
      { id: 2, nombre: 'Mouse Inalámbrico', precio: 350.0, existencia: 3 }
    ];
    component.productos = mockProductos;
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const filas = compiled.querySelectorAll('tbody tr');
    expect(filas.length).toBe(2);

    const fila1 = filas[0].textContent;
    expect(fila1).toContain('Teclado Mecánico');
    expect(fila1).toContain('12 unidades');

    const fila2 = filas[1].textContent;
    expect(fila2).toContain('Mouse Inalámbrico');
    expect(fila2).toContain('3 unidades');

    const alertaStock = compiled.querySelector('.alerta-stock');
    expect(alertaStock).toBeTruthy();
    expect(alertaStock?.textContent).toContain('3 unidades');
  });
});
