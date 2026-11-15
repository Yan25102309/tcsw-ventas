import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { of, throwError } from 'rxjs';
import { ProductosPageComponent } from './productos-page.component';
import { ProductosService } from '../../services/productos.service';
import { Producto } from '../../models/producto.model';

describe('ProductosPageComponent', () => {
  let component: ProductosPageComponent;
  let fixture: ComponentFixture<ProductosPageComponent>;
  let productosServiceSpy: jasmine.SpyObj<ProductosService>;

  beforeEach(async () => {
    productosServiceSpy = jasmine.createSpyObj('ProductosService', ['obtenerTodos']);

    await TestBed.configureTestingModule({
      imports: [RouterTestingModule, ProductosPageComponent],
      providers: [
        { provide: ProductosService, useValue: productosServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ProductosPageComponent);
    component = fixture.componentInstance;
  });

  it('debe crear el componente coordinador de página', () => {
    expect(component).toBeTruthy();
  });

  it('debe cambiar a estado "success" cuando la API retorna productos', () => {
    const mockProductos: Producto[] = [
      { id: 101, nombre: 'Monitor 24', precio: 2500, existencia: 8 }
    ];
    productosServiceSpy.obtenerTodos.and.returnValue(of(mockProductos));

    fixture.detectChanges();

    expect(component.estadoUi.tipo).toBe('success');
    expect(component.productos.length).toBe(1);
    expect(component.productos[0].nombre).toBe('Monitor 24');

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('app-producto-list')).toBeTruthy();
  });

  it('debe cambiar a estado "empty" cuando la API retorna un arreglo vacío', () => {
    productosServiceSpy.obtenerTodos.and.returnValue(of([]));

    fixture.detectChanges();

    expect(component.estadoUi.tipo).toBe('empty');
    expect(component.productos.length).toBe(0);

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.estado-vacio')).toBeTruthy();
    expect(compiled.textContent).toContain('No hay productos registrados');
  });

  it('debe cambiar a estado "error" cuando la API falla', () => {
    const mensajeError = 'No fue posible conectar con la API REST.';
    productosServiceSpy.obtenerTodos.and.returnValue(throwError(() => new Error(mensajeError)));

    fixture.detectChanges();

    expect(component.estadoUi.tipo).toBe('error');
    expect(component.estadoUi.mensajeError).toBe(mensajeError);

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.estado-error')).toBeTruthy();
    expect(compiled.textContent).toContain(mensajeError);
  });
});
