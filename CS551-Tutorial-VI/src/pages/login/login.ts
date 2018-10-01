import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { HomePage } from "../home/home";

/**
 * Generated class for the LoginPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-login',
  templateUrl: 'login.html',
})
export class LoginPage {
  public yourLabelVariable:string;
  public temp:string="";
  constructor(public navCtrl: NavController, public navParams: NavParams) {
    // this.yourLabelVariable=navParams.get('name');
    //alert(navParams.get('name'));
    this.temp=navParams.get('name');
    alert(this.temp);
    //this.yourLabelVariable=this.temp;
  }
   
  
  ionViewDidLoad() {
    console.log('ionViewDidLoad LoginPage');
  }

  signOut()
  {
    this.navCtrl.push(HomePage);
  }
  //this.yourLabelVariable=this.temp;
}
