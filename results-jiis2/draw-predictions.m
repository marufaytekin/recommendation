# ====================================================
# Octave gnuplot figure draw functions
# Maruf Aytekin <aaytekin@gmail.com>
# ====================================================
 function write (f, file_name)
   H = 3.2; W = 4;
   h = f;
   set (h,'papertype', '<custom>')
   set (h,'paperunits','inches');
   set (h,'papersize',[H W])
   set (h,'paperposition', [0,0,[W H]])
   set (h,'defaultaxesposition', [0.15, 0.15, 0.75, 0.75])
   set (0,'defaultaxesfontsize', 10)
   out_file_name = strcat (file_name,'.eps');
   print('-depsc', out_file_name);
 endfunction


# ========================================================
# MAE and Runtime
# Hash Tables
# ========================================================

x = 1:10;
n = 10;
f= figure();
y1 = UBKNNMaeList;
y2 = IBKNNMaeList;
y3 = UBKNNLSHHashTablesMaeList;
y4 = IBKNNLSHHashTablesMaeList;
y5 = UBLSH1HashTablesMaeList;
y6 = UBLSH2HashTablesMaeList;
y7 = IBLSH1HashTablesMaeList;
y8 = IBLSH2HashTablesMaeList;
plot ([1,n], [y1, y1],'-.k', [1,n], [y2, y2],'--k', x, y3,'-dk', x, y4,'-*k', x, y5,'-+k', x, y6,'-xk', x, y7,'-ok', x, y8,'-<k');
h = legend('UBP-CF', 'IBP-CF', 'UBP-LSH', 'IBP-LSH', 'UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northeast');
set (h, 'fontsize', 8);
xlabel("L");
ylabel('MAE');
xlim([1, 10]);
#ylim([1 8]);
legend boxon;
write(f, strcat(fileName,'-hash-tables-mae'));


f= figure();
x = 1:10;
y1 = UBKNNRuntimeList;
y2 = IBKNNRuntimeList;
y3 = UBKNNLSHHashTablesRuntimeList;
y4 = IBKNNLSHHashTablesRuntimeList;
y5 = UBLSH1HashTablesRuntimeList;
y6 = UBLSH2HashTablesRuntimeList;
y7 = IBLSH1HashTablesRuntimeList;
y8 = IBLSH2HashTablesRuntimeList;
plot (x, y3,'-dk', x, y4,'-*k', x, y5,'-+k', x, y6,'-xk', x, y7,'-ok', x, y8,'-<k');
h = legend('UBP-LSH', 'IBP-LSH','UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northwest');
set (h, 'fontsize', 8);
xlabel("L");
ylabel('Run Time(ms)');
xlim([1, 10])
legend boxon;
write(f, strcat(fileName,'-hash-tables-runtime'));

f= figure();
plot ( x, y5,'-+k', x, y6,'-xk', x, y7,'-ok', x, y8,'-<k');
h = legend('UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northwest');
set (h, 'fontsize', 8);
xlabel("L");
ylabel('Run Time(ms.)');
xlim([1, 10])
#ylim([0 0.4]);
legend boxon;
write(f, strcat(fileName,'-hash-tables-runtime2'));



# ========================================================
# MAE and Runtime
# Hash Functions
# ========================================================

x = 1:10;
n = 10;
f= figure();
y1 = UBKNNMaeList;
y2 = IBKNNMaeList;
y3 = UBKNNLSHHashFunctionsMaeList;
y4 = IBKNNLSHHashFunctionsMaeList;
y5 = UBLSH1HashFunctionsMaeList;
y6 = UBLSH2HashFunctionsMaeList;
y7 = IBLSH1HashFunctionsMaeList;
y8 = IBLSH2HashFunctionsMaeList;
plot ([1,n], [y1, y1],'-.k', [1,n], [y2, y2],'--k', x, y3,'-dk', x, y4,'-*k', x, y5,'-+k', x, y6,'-xk', x, y7,'-ok', x, y8,'-<k');
h = legend('UBP-CF', 'IBP-CF', 'UBP-LSH', 'IBP-LSH', 'UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northwest');
set (h, 'fontsize', 8);
xlim([1, 10]);
#ylim([1 4]);
xlabel("K");
ylabel('MAE');
legend boxon;
write(f, strcat(fileName,'-hash-functions-mae'));

f= figure();
x = 1:10;
y1 = UBKNNRuntimeList;
y2 = IBKNNRuntimeList;
y3 = UBKNNLSHHashFunctionsRuntimeList;
y4 = IBKNNLSHHashFunctionsRuntimeList;
y5 = UBLSH1HashFunctionsRuntimeList;
y6 = UBLSH2HashFunctionsRuntimeList;
y7 = IBLSH1HashFunctionsRuntimeList;
y8 = IBLSH2HashFunctionsRuntimeList;
plot (x, y3,'-dk', x, y4,'-*k', x, y5,'-+k', x, y6,'-xk', x, y7,'-ok', x, y8,'-<k');
h = legend( 'UBP-LSH', 'IBP-LSH', 'UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northeast');
set (h, 'fontsize', 8);
xlabel("K");
ylabel('Run Time(ms)');
xlim([1, 10])
legend boxon;
write(f, strcat(fileName,'-hash-functions-runtime'));

f= figure();
plot (x, y5,'-+k', x, y6,'-xk', x, y7,'-ok', x, y8,'-<k');
h = legend('UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northeast');
set (h, 'fontsize', 8);
xlabel("K");
ylabel('Run Time(ms.)');
xlim([1, 10])
#ylim([0 0.4]);
legend boxon;
write(f, strcat(fileName,'-hash-functions-runtime2'));


