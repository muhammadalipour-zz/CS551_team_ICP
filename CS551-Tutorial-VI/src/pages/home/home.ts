import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { LoginPage } from '../login/login';
import { RegisterPage } from '../register/register';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {
  public usrname :string;
  public pwd :string;
  constructor(public navCtrl: NavController) {

  }

    signIn() {
        var validpwd=localStorage.getItem(this.usrname);
        if(validpwd==this.pwd)
        {
            this.navCtrl.push(LoginPage,{name:this.usrname});
        }
        
    }
    
    Register(){
        this.navCtrl.push(RegisterPage);
    }
}
