package com.example.mynavjava;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import static java.util.logging.Logger.global;

public class MoreFragment extends Fragment {



    private final String[] texttt = {"Mossodmakkkk", "Pongngai", "Peepmax", "Skywalker", "Nongtiny"};
    private int i = 0;
    private int[] num = new int[texttt.length];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_more, container, false);
        TextView help = view.findViewById(R.id.help_button);
        RelativeLayout more = view.findViewById(R.id.fragment_more);

        //Test
        Button n =  new Button(getContext());
        n.setText("Testing");
        more.addView(n);

        n.setGravity(View.TEXT_ALIGNMENT_CENTER);

        //final String[] texttt = {"Mossodmak", "Pongngai", "Peepmax", "Skywalker", "Nongtiny"};
        TextView[] tv = new TextView[texttt.length];
        RelativeLayout.LayoutParams[] paramsRay = new RelativeLayout.LayoutParams[texttt.length];

        //Creating with for loop !!! <<Concentrate Now!>>
        int count = 50;
        for(i = 0; i < tv.length; i++){
            tv[i] = new TextView(getContext());
            tv[i].setText(texttt[i]);
            tv[i].setTextSize(30);
            tv[i].setLinksClickable(true);

            // This step is adding to the Layout <Crucial>
            // 1. Set position by >>RelativeLayout.LayoutParams<<
            paramsRay[i] = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            //paramsRay[i].addRule();
            paramsRay[i].setMargins(300, count+=200, 0, 0);

            more.addView(tv[i], paramsRay[i]);

            help.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
        return view;
    }
}
