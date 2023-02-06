import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaeventosComponent } from './listaeventos.component';

describe('ListaeventosComponent', () => {
  let component: ListaeventosComponent;
  let fixture: ComponentFixture<ListaeventosComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ListaeventosComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListaeventosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
