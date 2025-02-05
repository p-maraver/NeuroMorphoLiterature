// disable-link.directive.ts
import { Directive, HostListener, Input, Renderer2, ElementRef } from '@angular/core';

@Directive({
  selector: '[appDisableLink]'
})
export class DisableLinkDirective {
  @Input() appDisableLink = false;

  constructor(private el: ElementRef, private renderer: Renderer2) {}

  @HostListener('click', ['$event'])
  handleClick(event: Event): void {
    if (this.appDisableLink) {
      event.preventDefault();
      event.stopImmediatePropagation();
    }
  }

  ngOnChanges(): void {
    if (this.appDisableLink) {
      this.renderer.setStyle(this.el.nativeElement, 'pointer-events', 'none');
      this.renderer.setStyle(this.el.nativeElement, 'color', 'gray');
      this.renderer.setAttribute(this.el.nativeElement, 'aria-disabled', 'true');
    } else {
      this.renderer.removeStyle(this.el.nativeElement, 'pointer-events');
      this.renderer.removeStyle(this.el.nativeElement, 'color');
      this.renderer.removeAttribute(this.el.nativeElement, 'aria-disabled');
    }
  }
}
