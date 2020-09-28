package com.example.itrasher;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        //item click

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mClickListener.onItemClick(view, getAdapterPosition());

            }
        });

        //item long click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                mClickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });
    }

    public void setDetails (Context ctx, String descricao, String unidade,
                            String quantidade, String valor, String data, String image,
                            String Uid, String Email){

        TextView mDescricao = mView.findViewById(R.id.rDescricao);
        TextView mQuantidade  = mView.findViewById(R.id.rQuantidade);
        TextView mUnidade = mView.findViewById(R.id.rUnidade);
        TextView mValor = mView.findViewById(R.id.rValor);
        TextView mData = mView.findViewById(R.id.rData);
        TextView mUrlImagem= mView.findViewById(R.id.rTextUrlImagem);
        TextView mUid= mView.findViewById(R.id.textview_uid);
        TextView mEmail= mView.findViewById(R.id.textview_email);

        mDescricao.setText(descricao);
        mQuantidade.setText(quantidade);
        mUnidade.setText(unidade);
        mValor.setText(valor);
        mData.setText(data);
        mUrlImagem.setText(image);
        mUid.setText(Uid);
        mEmail.setText(Email);
    }

    private ViewHolder.ClickListener mClickListener;

    public interface ClickListener{

        void onItemClick (View view,int position);
        void onItemLongClick (View view, int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener){

        mClickListener = clickListener;
    }

}
