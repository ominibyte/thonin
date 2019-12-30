package dev.thonin.test;

import dev.thonin.annotations.Logger;
import org.sputnikdev.bluetooth.URL;
import org.sputnikdev.bluetooth.manager.BluetoothManager;
import org.sputnikdev.bluetooth.manager.impl.BluetoothManagerBuilder;

@Logger(id="names", type = String.class)
@Logger(id="cars", type = String.class)
public class Main {

    private static final String[] names = {"Richboy", "Kaycee", "David"};

    public Main() {
        BluetoothManager manager = new BluetoothManagerBuilder()
                .withTinyBTransport(true)
                //.withBlueGigaTransport("^*.$")
                .build();

        URL url = new URL("/dc:a6:32:17:ee:ae/ed:73:8d:72:39:19".toUpperCase() + "/1800/2a00");
        manager.getCharacteristicGovernor(url, true).addValueListener(value -> {
            for (byte b : value) System.out.println(b);
        });

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        new Main();
    }
}
