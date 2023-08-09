package com.example.shopeaze;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import android.text.TextUtils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.text.Editable;
import android.view.View;
import android.widget.EditText;

@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {
    @Mock
    LoginContract.View view;
    @Mock
    LoginContract.Model model;

    @Test
    public void nullViewObject(){               //works
        assertEquals(view.getEmail(), null);
    }
    @Test
    public void checkEmptyUsername(){
        when(view.getEmail()).thenReturn("");
        LoginContract.Presenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();
        verify(view).showErrorMessage("Please enter email");
    }
    @Test
    public void checkEmptyPassword(){
        when(view.getEmail()).thenReturn("anyemail@email.com");
        when(view.getPassword()).thenReturn("");
        LoginContract.Presenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();
        verify(view).showErrorMessage("Please enter password");

    }
    @Test
    public void emptyEmailOrder(){
        when(view.getEmail()).thenReturn("");
        LoginContract.Presenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();
        InOrder order = inOrder(view);
        order.verify(view).getEmail();
        order.verify(view).showErrorMessage("Please enter email");
    }

   /*
  left to test:
    test login success (succeeds with correct credentials)
    test login fail (fails with wrong credentials) */

    @Test
    public void userFound(){
       when(view.getEmail()).thenReturn("shopper1@sample.com");
       when(view.getPassword()).thenReturn("123456");
       //...code that idk yet...
        LoginContract.Presenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();
        verify(view).showLoginSuccessMessage();


    }

    @Test
    public void userNotFound(){
        when(view.getEmail()).thenReturn("foobar@email.com");
        when(view.getPassword()).thenReturn("123456");
        //...code that idk yet...
        LoginContract.Presenter presenter = new LoginPresenter(view, model);
        presenter.loginUser();
        verify(view).showErrorMessage("Authentication failed");

    }

}