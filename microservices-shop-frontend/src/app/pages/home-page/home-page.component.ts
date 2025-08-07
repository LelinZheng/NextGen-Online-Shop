import {Component, inject, OnInit} from '@angular/core';
import {OidcSecurityService} from "angular-auth-oidc-client";
import {Product} from "../../model/product";
import {ProductService} from "../../services/product/product.service";
import {AsyncPipe, JsonPipe} from "@angular/common";
import {Router} from "@angular/router";
import {Order} from "../../model/order";
import {FormsModule} from "@angular/forms";
import {OrderService} from "../../services/order/order.service";

@Component({
  selector: 'app-homepage',
  templateUrl: './home-page.component.html',
  standalone: true,
  imports: [
    AsyncPipe,
    JsonPipe,
    FormsModule
  ],
  styleUrl: './home-page.component.css'
})
export class HomePageComponent implements OnInit {
  private readonly oidcSecurityService = inject(OidcSecurityService);
  private readonly productService = inject(ProductService);
  private readonly orderService = inject(OrderService);
  private readonly router = inject(Router);
  isAuthenticated = false;
  products: Array<Product> = [];
  quantityIsNull = false;
  orderSuccess = false;
  orderFailed = false;

  ngOnInit(): void {
    this.oidcSecurityService.isAuthenticated$.subscribe(
      ({isAuthenticated}) => {
        this.isAuthenticated = isAuthenticated;
        this.productService.getProducts()
        .pipe()
        .subscribe(product => {
          this.products = product;
        })
      }
    )
  }

  goToCreateProductPage() {
    this.router.navigateByUrl('/add-product');
  }

  orderProduct(product: Product, quantity: string) {

    this.oidcSecurityService.userData$.subscribe(result => {

      // const userDetails = {
      //   email: result.userData.email,
      //   firstName: result.userData.firstName,
      //   lastName: result.userData.lastName
      // };
      // console.log('Final userDetails:', JSON.stringify(userDetails, null, 2));
      // console.log('Extracted user details:', userDetails);

      if(!quantity) {
        this.orderFailed = true;
        this.orderSuccess = false;
        this.quantityIsNull = true;
      } else {
        const order: Order = {
          skuCode: product.skuCode,
          price: product.price,
          quantity: Number(quantity),
          // userDetails: userDetails
        }

        console.log('Order object being sent:', JSON.stringify(order, null, 2)); // ✅ Debug log
        console.log('Product object:', JSON.stringify(product, null, 2)); // ✅ Check product structure

        this.orderService.orderProduct(order).subscribe(() => {
          this.orderSuccess = true;
          this.orderFailed = false;
        }, error => {
          console.error('Full error object:', error); // ✅ See full error
          console.error('Error status:', error.status);
          console.error('Error message:', error.message);
          console.error('Error body:', error.error);
          this.orderFailed = true;
          this.orderSuccess = false;
        })
      }
    })
  }
}
