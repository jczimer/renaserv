import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CadtipoeventosComponent } from './cadtipoeventos.component';

describe('CadtipoeventosComponent', () => {
  let component: CadtipoeventosComponent;
  let fixture: ComponentFixture<CadtipoeventosComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CadtipoeventosComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CadtipoeventosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
