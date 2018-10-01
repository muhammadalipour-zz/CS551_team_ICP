import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { HomePage } from '../home/home';

/**
 * Generated class for the RegisterPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-register',
  templateUrl: 'register.html',
})
export class RegisterPage {
  public username: string ;
  public password: string ;
  public fname: string ;
  public lname: string ;
  public cpwd: string ;
  constructor(public navCtrl: NavController, public navParams: NavParams) {
  }

  ionViewDidLoad() {
    console.log('ionViewDidLoad RegisterPage');
  }

  signUp()
  {
    if(this.username !=null && this.password!=null && this.fname!=null && this.lname!=null && this.cpwd!=null )
    {
      if(this.password == this.cpwd)
      {
        localStorage.setItem(this.username,this.password);
        alert("sign up done");
        this.navCtrl.push(HomePage);
      }
      else
      {
        alert("Password doesnot match ");
      }
    }
    else
    {
      alert("Some fields are empty");
    }
  }

}
