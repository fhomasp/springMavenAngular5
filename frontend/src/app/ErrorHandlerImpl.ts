import {ErrorHandler, Injectable, Injector, ViewContainerRef} from '@angular/core';
import {UNAUTHORIZED, BAD_REQUEST, FORBIDDEN} from 'http-status-codes';
import {ToastService} from './toast-service';

@Injectable()
export class ErrorHandlerImpl implements ErrorHandler {

  static readonly REFRESH_PAGE_ON_TOAST_CLICK_MESSAGE: string = 'An error occurred: ';

  constructor(private injector: Injector) {}


  public handleError(error: any) {
    console.error('handled: ' + error);
    const httpErrorCode = error.httpErrorCode;

    switch (httpErrorCode) {
      case UNAUTHORIZED:
        this.showError(error.message);
        break;
        // this.router.navigateByUrl('/login');
      case FORBIDDEN:
        // this.router.navigateByUrl('/unauthorized');
        this.showError(error.message);
        break;
      case BAD_REQUEST:
        this.showError(error.message);
        break;
      default:
        this.showError(ErrorHandlerImpl.REFRESH_PAGE_ON_TOAST_CLICK_MESSAGE + error);
    }
  }

  private showError(message: string) {
    const toastService = this.injector.get(ToastService);
    toastService.showError(message);
  }
}
