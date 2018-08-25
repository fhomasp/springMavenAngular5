import {Injectable} from '@angular/core';
import {ToastsManager} from 'ng2-toastr';

@Injectable()
export class ToastService {

  static readonly DEFAULT_ERROR_TITLE: string = 'Something went wrong';

  constructor(private toastManager: ToastsManager) {
  }


  showError(message: string) {

    this.toastManager.error(message, ToastService.DEFAULT_ERROR_TITLE,
      { dismiss: 'click' , positionClass: 'toast-top-full-width' , showCloseButton: true });
    //   .then(
    //     (toast: Toast) => {
    //   const currentToastId: number = toast.id;
    //   this.toastManager.onClickToast().subscribe(clickedToast => {
    //     if (clickedToast.id === currentToastId) {
    //       console.log('toast dismiss: ' + clickedToast.id + ' - ' + currentToastId);
    //       this.toastManager.dismissToast(toast);
    //       // this.router.navigate(['/checklist']);
    //     }
    //   });
    // });
  }

}
