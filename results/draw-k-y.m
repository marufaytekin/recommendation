

# ========================================================
# k and MAE/runtime
# Test case: 5/6/7/07
# ========================================================

y1=UBMaeList;
y2=IBMaeList;
x= 1:5:50;

f= figure();
plot (x,y1,'-dk', x,y2, '--ok');
legend('UBP-CF', 'IBP-CF')
xlabel("k")
ylabel("MAE")
write(f, strcat(fileName,'-mae-k'));


# ========================================================
# y and MAE
# Test case: 500/520
# ========================================================

y1=UBMaeList;
y2=IBMaeList;
x= 1:3:30;

f= figure();
plot (x,y1,'-dk', x, y2, '-*k')
legend('UBP-CF', 'IBP-CF')
xlabel("Significance(Y)")
ylabel("MAE")
write(f, strcat(fileName,'-mae-y'));
