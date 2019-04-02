package com.jevon.passwordbook.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.jevon.passwordbook.R;
import com.jevon.passwordbook.listener.RecyclerViewClickListener;
import com.jevon.passwordbook.been.Password;
import com.jevon.passwordbook.databinding.ItemPasswordBinding;
import com.jevon.passwordbook.utils.Jlog;
import com.jevon.passwordbook.viewmodel.MainViewModel;

import java.util.List;

/**
 * @Author: Mr.J
 * @CreateDate: 2019/3/29 11:18
 */
public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ViewHolder> {

    private final ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
    private List<Password> mList;
    private MainViewModel viewModel;

    public MainListAdapter(MainViewModel mainViewModel, List<Password> list) {
        this.viewModel = mainViewModel;
        this.mList = list;
    }

    @NonNull
    @Override
    public MainListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemPasswordBinding binding
                = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_password, viewGroup, false);
        ViewHolder holder = new ViewHolder(binding.getRoot().getRootView());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainListAdapter.ViewHolder viewHolder, int i) {
        Password password = mList.get(i);
        ItemPasswordBinding binding = viewHolder.getBinding();
        binding.setItem(password);
        binding.setAdapter(this);
        TextDrawable textDrawable = TextDrawable.builder().beginConfig().toUpperCase().endConfig()
                .buildRound(password.getName().substring(0, 1), colorGenerator.getColor(password.getName()));
        binding.circleViewIcon.setImageDrawable(textDrawable);
        binding.setPosition(i);
        binding.setViewModel(viewModel);

        //设置查看按钮触摸事件
        final TextView tvPassword = binding.tvPassword;
        binding.ivEye.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    tvPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    tvPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                return true;
            }
        });
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private ItemPasswordBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public ItemPasswordBinding getBinding() {
            return binding;
        }

        public void setBinding(ItemPasswordBinding binding) {
            this.binding = binding;
        }
    }
}
