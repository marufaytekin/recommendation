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

x = 4:13;
n = 13;
f= figure();
y1 = UBKNNMaeList;
y2 = IBKNNMaeList;
y3 = UBKNNLSHHashTablesMaeList;
y4 = IBKNNLSHHashTablesMaeList;
y5 = UBLSH1HashTablesMaeList;
y6 = UBLSH2HashTablesMaeList;
y7 = IBLSH1HashTablesMaeList;
y8 = IBLSH2HashTablesMaeList;
plot ([4,n], [y1, y1],'-.k', [4,n], [y2, y2],'--k', x, y3,'-dk', x, y4,'-*k', x, y5,'-+k', x, y6,'->k', x, y7,'-ok', x, y8,'-<k');
h = legend('UBP-CF', 'IBP-CF', 'UBP-LSH', 'IBP-LSH', 'UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northeast');
set (h, 'fontsize', 8);
xlabel("L");
ylabel('MAE');
xlim([4, 13]);
#ylim([0.76 0.90]);
legend boxon;
write(f, strcat(fileName,'-hash-tables-mae'));


f= figure();
x = 4:13;
y1 = UBKNNRuntimeList;
y2 = IBKNNRuntimeList;
y3 = UBKNNLSHHashTablesRuntimeList;
y4 = IBKNNLSHHashTablesRuntimeList;
y5 = UBLSH1HashTablesRuntimeList;
y6 = UBLSH2HashTablesRuntimeList;
y7 = IBLSH1HashTablesRuntimeList;
y8 = IBLSH2HashTablesRuntimeList;
plot (x, y3,'-dk', x, y4,'-*k', x, y5,'-+k', x, y6,'->k', x, y7,'-ok', x, y8,'-<k');
h = legend('UBP-LSH', 'IBP-LSH','UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northwest');
set (h, 'fontsize', 8);
xlabel("L");
ylabel('Run Time(ms)');
xlim([4, 13])
legend boxon;
write(f, strcat(fileName,'-hash-tables-runtime'));

f= figure();
plot ( x, y5,'-+k', x, y6,'->k', x, y7,'-ok', x, y8,'-<k');
h = legend('UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northwest');
set (h, 'fontsize', 8);
xlabel("L");
ylabel('Run Time(ms.)');
xlim([4, 13])
legend boxon;
write(f, strcat(fileName,'-hash-tables-runtime2'));

f= figure();
plot ( x, y5,'-+k', x, y6,'->k');
h = legend('UBP-LSH1', 'UBP-LSH2', 'location', 'northwest');
set (h, 'fontsize', 8);
xlabel("L");
ylabel('Run Time(ms.)');
xlim([4, 13])
legend boxon;
write(f, strcat(fileName,'-hash-tables-runtime2-ub'));

f= figure();
plot (x, y7,'-ok', x, y8,'-<k');
h = legend('IBP-LSH1', 'IBP-LSH2', 'location', 'northwest');
set (h, 'fontsize', 8);
xlabel("L");
ylabel('Run Time(ms.)');
xlim([4, 13])
legend boxon;
write(f, strcat(fileName,'-hash-tables-runtime2-ib'));

f= figure();
y3 = UBKNNLSHHashTablesCandidateSetList;
y4 = IBKNNLSHHashTablesCandidateSetList;
y5 = UBLSH1HashTablesCandidateSetList;
y6 = UBLSH2HashTablesCandidateSetList;
y7 = IBLSH1HashTablesCandidateSetList;
y8 = IBLSH2HashTablesCandidateSetList;
plot (x, y3,'-dk', x, y4,'-*k',x, y5,'-+k', x, y6,'->k', x, y7,'-ok', x, y8,'-<k');
h = legend('UBP-LSH', 'IBP-LSH', 'UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northwest');
set (h, 'fontsize', 8);
xlabel("L");
ylabel('Candidate set size');
xlim([4, 13])
legend boxon;
write(f, strcat(fileName,'-hash-tables-candidate-set'));


# ========================================================
# MAE and Runtime
# Hash Functions
# ========================================================

x = 4:13;
n = 13;
f= figure();
y1 = UBKNNMaeList;
y2 = IBKNNMaeList;
y3 = UBKNNLSHHashFunctionsMaeList;
y4 = IBKNNLSHHashFunctionsMaeList;
y5 = UBLSH1HashFunctionsMaeList;
y6 = UBLSH2HashFunctionsMaeList;
y7 = IBLSH1HashFunctionsMaeList;
y8 = IBLSH2HashFunctionsMaeList;
plot ([4,n], [y1, y1],'-.k', [4,n], [y2, y2],'--k', x, y3,'-dk', x, y4,'-*k', x, y5,'-+k', x, y6,'->k', x, y7,'-ok', x, y8,'-<k');
h = legend('UBP-CF', 'IBP-CF', 'UBP-LSH', 'IBP-LSH', 'UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northwest');
set (h, 'fontsize', 8);
xlim([4, 13]);
#ylim([0.76 0.90]);
xlabel("K");
ylabel('MAE');
legend boxon;
write(f, strcat(fileName,'-hash-functions-mae'));

f= figure();
x = 4:13;
y1 = UBKNNRuntimeList;
y2 = IBKNNRuntimeList;
y3 = UBKNNLSHHashFunctionsRuntimeList;
y4 = IBKNNLSHHashFunctionsRuntimeList;
y5 = UBLSH1HashFunctionsRuntimeList;
y6 = UBLSH2HashFunctionsRuntimeList;
y7 = IBLSH1HashFunctionsRuntimeList;
y8 = IBLSH2HashFunctionsRuntimeList;
plot (x, y3,'-dk', x, y4,'-*k', x, y5,'-+k', x, y6,'->k', x, y7,'-ok', x, y8,'-<k');
h = legend( 'UBP-LSH', 'IBP-LSH', 'UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northeast');
set (h, 'fontsize', 8);
xlabel("K");
ylabel('Run Time(ms)');
xlim([4, 13])
legend boxon;
write(f, strcat(fileName,'-hash-functions-runtime'));

f= figure();
plot (x, y5,'-+k', x, y6,'->k', x, y7,'-ok', x, y8,'-<k');
h = legend('UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northeast');
set (h, 'fontsize', 8);
xlabel("K");
ylabel('Run Time(ms.)');
xlim([4, 13])
legend boxon;
write(f, strcat(fileName,'-hash-functions-runtime2'));

f= figure();
plot (x, y5,'-+k', x, y6,'->k');
h = legend('UBP-LSH1', 'UBP-LSH2', 'location', 'northeast');
set (h, 'fontsize', 8);
xlabel("K");
ylabel('Run Time(ms.)');
xlim([4, 13])
legend boxon;
write(f, strcat(fileName,'-hash-functions-runtime2-ub'));

f= figure();
plot (x, y7,'-ok', x, y8,'-<k');
h = legend('IBP-LSH1', 'IBP-LSH2', 'location', 'northeast');
set (h, 'fontsize', 8);
xlabel("K");
ylabel('Run Time(ms.)');
xlim([4, 13])
legend boxon;
write(f, strcat(fileName,'-hash-functions-runtime2-ib'));

f= figure();
y3 = UBKNNLSHHashFunctionsCandidateSetList;
y4 = IBKNNLSHHashFunctionsCandidateSetList;
y5 = UBLSH1HashFunctionsCandidateSetList;
y6 = UBLSH2HashFunctionsCandidateSetList;
y7 = IBLSH1HashFunctionsCandidateSetList;
y8 = IBLSH2HashFunctionsCandidateSetList;
plot (x, y3,'-dk', x, y4,'-*k',x, y5,'-+k', x, y6,'->k', x, y7,'-ok', x, y8,'-<k');
h = legend('UBP-LSH', 'IBP-LSH', 'UBP-LSH1', 'UBP--LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northeast');
set (h, 'fontsize', 8);
xlabel("K");
ylabel('Candidate set size');
xlim([4, 13])
legend boxon;
write(f, strcat(fileName,'-hash-functions-candidate-set'));

f= figure();
total_test_data = max(UBKNNLSHHashFunctionsPredictedItemsList);
y3 = UBKNNLSHHashFunctionsPredictedItemsList/total_test_data;
y4 = IBKNNLSHHashFunctionsPredictedItemsList/total_test_data;
y5 = UBLSH1HashFunctionsPredictedItemsList/total_test_data;
y6 = UBLSH2HashFunctionsPredictedItemsList/total_test_data;
y7 = IBLSH1HashFunctionsPredictedItemsList/total_test_data;
y8 = IBLSH2HashFunctionsPredictedItemsList/total_test_data;
plot (x, y3,'-dk', x, y4,'-*k',x, y5,'-+k', x, y6,'->k', x, y7,'-ok', x, y8,'-<k');
h = legend('UBP-LSH', 'IBP-LSH', 'UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'west');
set (h, 'fontsize', 8);
xlabel("K");
ylabel('Prediction Ratio');
xlim([4, 13])
ylim([0 1])
write(f, strcat(fileName,'-hash-functions-prediction-coverage'));


# ========================================================
# Model build time
# Test case: 8
# ========================================================

# Running time

x = UBnumOfUsersList/1000;
x2 = IBnumOfItemsList/1000;
y1 = UBmodelBuildTimeList/1000;
y2 = IBmodelBuildTimeList/1000;
y3 = UBLSHmodelBuildTimeList/1000;
y4 = IBLSHmodelBuildTimeList/1000;

f= figure();
plot (x, y1, '-dk', x, y3, '--*k');
legend("UBP-CF","UBP-LSH", "location", "northwest");
xlabel("users(x1000)")
ylabel("Model Build Time (sec)");
xlim([x(1) x(10)])
write(f, strcat(fileName,'-model-build-time-ub'));

f= figure();
plot (x2, y2, '-dk', x2, y4, '--*k' );
legend("IBP-CF", "IBP-LSH", "location", "northwest");
xlabel("items(x1000)")
ylabel("Model Build Time(sec)");
xlim([x2(1) x2(10)])
write(f, strcat(fileName,'-model-build-time-ib'));




# ===========================================
# Heat-maps
# ===========================================

x = 1:10;
y = 1:10;
xx = [2 4 6 8 10]
yy = [2 4 6 8 10]

f= figure();
data = UBKNNLSHMae2D;
contourf(x, y, data);
axis square;
colorbar;
set(gca,'XTick',xx);
set(gca,'YTick',yy);
xlabel("K");
ylabel("L");
title("MAE");

file = strcat(fileName, '-ublsh-heat-map-mae');
write (f, file);


f= figure();
data = UBKNNLSHRuntime2D;
contourf(x, y, data);
axis square;
colorbar;
set(gca,'XTick',xx);
set(gca,'YTick',yy);
xlabel("K");
ylabel("L");
title("Running Time (ms)");
file = strcat(fileName, '-ublsh-heat-map-runtime');
write(f, file);


f= figure();
data = UBKNNLSHCandidateSetList2D;
contourf(x, y, data ./ 1000);
axis square;
colorbar;
set(gca,'XTick',xx);
set(gca,'YTick',yy);
xlabel("K");
ylabel("L");
title("Candidate Set Size (K)");
file = strcat(fileName, '-ublsh-heat-map-candidate');
write(f, file);
