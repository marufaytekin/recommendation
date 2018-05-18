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
l = 5
k = 6
x = 1:10;
n = 10;
f= figure();
y1 = UBKNNMaeList;
y2 = IBKNNMaeList;
y3 = UBKNNLSHMae2D(:,k);
y4 = IBKNNLSHMae2D(:,k);
y5 = UBLSH1Mae2D(:,k);
y6 = UBLSH2Mae2D(:,k);
y7 = IBLSH1Mae2D(:,k);
y8 = IBLSH2Mae2D(:,k);
plot ([1,n], [y1, y1],'-.k', [1,n], [y2, y2],'--k', x, y3,'-dk', x, y4,'-*k', x, y5,'-+k', x, y6,'->k', x, y7,'-ok', x, y8,'-<k');
h = legend('UB-KNN', 'IB-KNN', 'UB-KNN-LSH', 'IB-KNN-LSH', 'UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northeast');
set (h, 'fontsize', 10);
xlabel('Number of Hash Tables');
ylabel('MAE');
xlim([1, 10]);
#ylim([1 1.45]);
legend boxon;
write(f, strcat(fileName,'-hash-tables-mae'));



f= figure();
y3 = UBKNNLSHRuntime2D(:,k);
y4 = IBKNNLSHRuntime2D(:,k);
y5 = UBLSH1Runtime2D(:,k);
y6 = UBLSH2Runtime2D(:,k);
y7 = IBLSH1Runtime2D(:,k);
y8 = IBLSH2Runtime2D(:,k);
plot (x, y3,'-dk', x, y4,'-*k', x, y5,'-+k', x, y6,'->k', x, y7,'-ok', x, y8,'-<k');
h = legend('UB-KNN-LSH', 'IB-KNN-LSH', 'UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northwest');
set (h, 'fontsize', 10);
xlabel('Number of Hash Tables');
ylabel('Run Time(ms)');
xlim([1, 10]);
legend boxon;
write(f, strcat(fileName,'-hash-tables-runtime'));



f= figure();
y5 = UBLSH1Runtime2D(:,k);
y6 = UBLSH2Runtime2D(:,k);
y7 = IBLSH1Runtime2D(:,k);
y8 = IBLSH2Runtime2D(:,k);
plot (x, y5,'-+k', x, y6,'->k', x, y7,'-ok', x, y8,'-<k');
h = legend('UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northwest');
set (h, 'fontsize', 10);
xlabel('Number of Hash Tables');
ylabel('Run Time(ms)');
xlim([1, 10]);
legend boxon;
write(f, strcat(fileName,'-hash-tables-runtime-2'));


# ========================================================
# MAE and Runtime
# Hash Functions
# ========================================================
l = 5
k = 6
x = 4:13;
n = 13;
f= figure();
y1 = UBKNNMaeList;
y2 = IBKNNMaeList;
y3 = UBKNNLSHMae2D(l,:);
y4 = IBKNNLSHMae2D(l,:);
y5 = UBLSH1Mae2D(l,:);
y6 = UBLSH2Mae2D(l,:);
y7 = IBLSH1Mae2D(l,:);
y8 = IBLSH2Mae2D(l,:);
plot ([4,n], [y1, y1],'-.k', [4,n], [y2, y2],'--k', x, y3,'-dk', x, y4,'-*k', x, y5,'-+k', x, y6,'->k', x, y7,'-ok', x, y8,'-<k');
h = legend('UB-KNN', 'IB-KNN', 'UB-KNN-LSH', 'IB-KNN-LSH', 'UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northwest');
set (h, 'fontsize', 10);
xlabel('Number of Hash Functions');
ylabel('MAE');
xlim([4, 13])
legend boxon;
write(f, strcat(fileName,'-hash-functions-mae'));

l = 5
k = 6
x = 4:13;
n = 10;
f= figure();
y3 = UBKNNLSHRuntime2D(l,:);
y4 = IBKNNLSHRuntime2D(l,:);
y5 = UBLSH1Runtime2D(l,:);
y6 = UBLSH2Runtime2D(l,:);
y7 = IBLSH1Runtime2D(l,:);
y8 = IBLSH2Runtime2D(l,:);
plot (x, y3,'-dk', x, y4,'-*k', x, y5,'-+k', x, y6,'->k', x, y7,'-ok', x, y8,'-<k');
h = legend('UB-KNN-LSH', 'IB-KNN-LSH', 'UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northeast');
set (h, 'fontsize', 10);
xlabel('Number of Hash Functions');
ylabel('Run Time(ms)');
xlim([4, 13])
legend boxon;
write(f, strcat(fileName,'-hash-functions-runtime'));


l = 5
k = 6
x = 4:13;
n = 10;
f= figure();
y5 = UBLSH1Runtime2D(l,:);
y6 = UBLSH2Runtime2D(l,:);
y7 = IBLSH1Runtime2D(l,:);
y8 = IBLSH2Runtime2D(l,:);
plot (x, y5,'-+k', x, y6,'->k', x, y7,'-ok', x, y8,'-<k');
h = legend('UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northeast');
set (h, 'fontsize', 10);
xlabel('Number of Hash Functions');
ylabel('Run Time(ms)');
xlim([4, 13])
legend boxon;
write(f, strcat(fileName,'-hash-functions-runtime-2'));

