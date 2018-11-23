package edu.oakland.nbayagich.investing;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.Random;

public class InvestingThread extends Thread {
    float accountNumber = 0;
    Handler mainActivityHandler = null;

    public InvestingThread(String accountNum, Handler handler) {
        this.accountNumber = Float.parseFloat(accountNum);
        mainActivityHandler = handler;
    }

}
