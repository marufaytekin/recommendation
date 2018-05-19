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
  set (0,'defaultaxesfontsize', 14)
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
h = legend('UB-KNN', 'IB-KNN', 'UB-KNN-LSH', 'IB-KNN-LSH', 'UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northeast');
set (h, 'fontsize', 10);
xlabel('Number of Hash Tables');
ylabel('MAE');
xlim([4, 13]);
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
h = legend('UB-KNN-LSH', 'IB-KNN-LSH','UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northwest');
set (h, 'fontsize', 10);
xlabel('Number of Hash Tables');
ylabel('Run Time(ms)');
xlim([4, 13])
legend boxon;
write(f, strcat(fileName,'-hash-tables-runtime'));

f= figure();
plot ( x, y5,'-+k', x, y6,'->k', x, y7,'-ok', x, y8,'-<k');
h = legend('UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northwest');
set (h, 'fontsize', 10);
xlabel('Number of Hash Tables');
ylabel('Run Time(ms.)');
xlim([4, 13])
legend boxon;
write(f, strcat(fileName,'-hash-tables-runtime2'));

f= figure();
plot ( x, y5,'-+k', x, y6,'->k');
h = legend('UBP-LSH1', 'UBP-LSH2', 'location', 'northwest');
set (h, 'fontsize', 10);
xlabel('Number of Hash Tables');
ylabel('Run Time(ms.)');
xlim([4, 13])
legend boxon;
write(f, strcat(fileName,'-hash-tables-runtime2-ub'));

f= figure();
plot (x, y7,'-ok', x, y8,'-<k');
h = legend('IBP-LSH1', 'IBP-LSH2', 'location', 'northwest');
set (h, 'fontsize', 10);
xlabel('Number of Hash Tables');
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
h = legend('UB-KNN-LSH', 'IB-KNN-LSH', 'UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northwest');
set (h, 'fontsize', 10);
xlabel('Number of Hash Tables');
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
h = legend('UB-KNN', 'IB-KNN', 'UB-KNN-LSH', 'IB-KNN-LSH', 'UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northwest');
set (h, 'fontsize', 10);
xlim([4, 13]);
xlabel('Number of Hash Functions');
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
h = legend( 'UB-KNN-LSH', 'IB-KNN-LSH', 'UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northeast');
set (h, 'fontsize', 10);
xlabel('Number of Hash Functions');
ylabel('Run Time(ms)');
xlim([4, 13])
legend boxon;
write(f, strcat(fileName,'-hash-functions-runtime'));

f= figure();
plot (x, y5,'-+k', x, y6,'->k', x, y7,'-ok', x, y8,'-<k');
h = legend('UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northeast');
set (h, 'fontsize', 10);
xlabel('Number of Hash Functions');
ylabel('Run Time(ms.)');
xlim([4, 13])
legend boxon;
write(f, strcat(fileName,'-hash-functions-runtime2'));

f= figure();
plot (x, y5,'-+k', x, y6,'->k');
h = legend('UBP-LSH1', 'UBP-LSH2', 'location', 'northeast');
set (h, 'fontsize', 10);
xlabel('Number of Hash Functions');
ylabel('Run Time(ms.)');
xlim([4, 13])
legend boxon;
write(f, strcat(fileName,'-hash-functions-runtime2-ub'));

f= figure();
plot (x, y7,'-ok', x, y8,'-<k');
h = legend('IBP-LSH1', 'IBP-LSH2', 'location', 'northeast');
set (h, 'fontsize', 10);
xlabel('Number of Hash Functions');
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
h = legend('UB-KNN-LSH', 'IB-KNN-LSH', 'UBP-LSH1', 'UBP--LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'northeast');
set (h, 'fontsize', 10);
xlabel('Number of Hash Functions');
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
h = legend('UB-KNN-LSH', 'IB-KNN-LSH', 'UBP-LSH1', 'UBP-LSH2', 'IBP-LSH1', 'IBP-LSH2', 'location', 'west');
set (h, 'fontsize', 10);
xlabel('Number of Hash Functions');
ylabel('Prediction Ratio');
xlim([4, 13])
ylim([0 1])
write(f, strcat(fileName,'-hash-functions-prediction-coverage'));


# ========================================================
# Number of Hash Functions on the metrics
# ========================================================

n = 13;
x = 4:13;
numOfHashTables = 10;

y1 = IBKNNRecommenderPrecision;
y2 = UBKNNRecommenderPrecision;
y3 = IBLSH1RecommenderHashFunctionsPrecision;
y4 = IBLSH2RecommenderHashFunctionsPrecision;
y5 = UBLSH1RecommenderHashFunctionsPrecision;
y6 = UBLSH2RecommenderHashFunctionsPrecision;
f= figure();
p = plot ([4,n], [y1, y1],'-.k', [4,n], [y2, y2],'--k',x, y3,'-+k', x, y4,'->k', x, y5,'-ok', x, y6,'-<k');
h = legend('IB-TOP-N', 'UB-TOP-N', 'IBR-LSH1', 'IBR-LSH2', 'UBR-LSH1', 'UBR-LSH2', 'location', 'northwest');
set (h, 'fontsize', 10);
legend boxon;
xlabel('Number of Hash Functions');
ylabel('Precision');
xlim([4, 13]);
#ylim([-0.01 0.06]);
write(f, strcat(fileName,'-hash-functions-precision'));

----------

n = 13;
x = 4:13;
numOfHashTables = 10;

y1 = IBKNNRecommenderDiversity;
y2 = UBKNNRecommenderDiversity;
y3 = IBLSH1RecommenderHashFunctionsDiversity;
y4 = IBLSH2RecommenderHashFunctionsDiversity;
y5 = UBLSH1RecommenderHashFunctionsDiversity;
y6 = UBLSH2RecommenderHashFunctionsDiversity;
f= figure();
plot ([4,n], [y1, y1],'-.k', [4,n], [y2, y2],'--k',x, y3,'-+k', x, y4,'->k', x, y5,'-ok', x, y6,'-<k');
h = legend('IB-TOP-N', 'UB-TOP-N', 'IBR-LSH1', 'IBR-LSH2', 'UBR-LSH1', 'UBR-LSH2', 'location', 'northwest');
set (h, 'fontsize', 10);
legend boxon;
xlabel('Number of Hash Functions');
ylabel('Diversity');
xlim([4, 13])
ylim([0.2 1.4])
write(f, strcat(fileName,'-hash-functions-diversity'));

-----------

n = 13;
x = 4:13;
numOfHashTables = 10;

y1 = IBKNNRecommenderAggrDiversity;
y2 = UBKNNRecommenderAggrDiversity;
y3 = IBLSH1RecommenderHashFunctionsAggrDiversity;
y4 = IBLSH2RecommenderHashFunctionsAggrDiversity;
y5 = UBLSH1RecommenderHashFunctionsAggrDiversity;
y6 = UBLSH2RecommenderHashFunctionsAggrDiversity;
f= figure();
plot ([4,n], [y1, y1],'-.k', [4,n], [y2, y2],'--k',x, y3,'-+k', x, y4,'->k', x, y5,'-ok', x, y6,'-<k');
h = legend('IB-TOP-N', 'UB-TOP-N', 'IBR-LSH1', 'IBR-LSH2', 'UBR-LSH1', 'UBR-LSH2', 'location', 'northwest');
set (h, 'fontsize', 10);
legend boxon;
xlabel('Number of Hash Functions');
ylabel('Aggregate Diversity');
xlim([4, 13])
write(f, strcat(fileName,'-hash-functions-aggrdiversity'));

-------------

n = 13;
x = 4:13;
numOfHashTables = 10;

y1 = IBKNNRecommenderNovelty;
y2 = UBKNNRecommenderNovelty;
y3 = IBLSH1RecommenderHashFunctionsNovelty;
y4 = IBLSH2RecommenderHashFunctionsNovelty;
y5 = UBLSH1RecommenderHashFunctionsNovelty;
y6 = UBLSH2RecommenderHashFunctionsNovelty;
f= figure();
plot ([4,n], [y1, y1],'-.k', [4,n], [y2, y2],'--k',x, y3,'-+k', x, y4,'->k', x, y5,'-ok', x, y6,'-<k');
h = legend('IB-TOP-N', 'UB-TOP-N', 'IBR-LSH1', 'IBR-LSH2', 'UBR-LSH1', 'UBR-LSH2', 'location', 'northwest');
set (h, 'fontsize', 10);
legend boxon;
xlabel('Number of Hash Functions');
ylabel('Novelty');
xlim([4, 13])
ylim([4 9])
write(f, strcat(fileName,'-hash-functions-novelty'));

-----------

max_x = n = 13;
x = 4:max_x;
y1 = IBKNNRecommenderAvgRecommTime;
y2 = UBKNNRecommenderAvgRecommTime;
y3 = IBLSH1RecommenderHashFunctionsAvgRecommTime;
y4 = IBLSH2RecommenderHashFunctionsAvgRecommTime;
y5 = UBLSH1RecommenderHashFunctionsAvgRecommTime;
y6 = UBLSH2RecommenderHashFunctionsAvgRecommTime;
f= figure();
plot (x, y3 ./ y4,'-+k', x, y5 ./ y6,'->k');
plot (x, y3,'-+k', x, y4,'->k', x, y5,'-ok', x, y6,'-<k');
h = legend('IBR-LSH1', 'IBR-LSH2', 'UBR-LSH1', 'UBR-LSH2', 'location', 'northeast');
set (h, 'fontsize', 10);
legend boxon;
xlim([4, 13])
xlabel('Number of Hash Functions');
ylabel('Avg Recc. Time (ms.)')
write(f, strcat(fileName,'-hash-functions-topn-runtime2'));

f= figure();
plot (x, y5,'-ok', x, y6,'-<k');
h = legend('UBR-LSH1', 'UBR-LSH2', 'location', 'northeast');
set (h, 'fontsize', 10);
legend boxon;
xlim([4, 13])
xlabel('Number of Hash Functions');
ylabel('Avg Recc. Time (ms.)')
write(f, strcat(fileName,'-hash-functions-topn-runtime2-ub'));

f= figure();
plot (x, y3,'-+k', x, y4,'->k');
h = legend('IBR-LSH1', 'IBR-LSH2', 'location', 'northeast');
set (h, 'fontsize', 10);
legend boxon;
xlim([4, 13])
xlabel('Number of Hash Functions');
ylabel('Avg Recc. Time (ms.)')
write(f, strcat(fileName,'-hash-functions-topn-runtime2-ib'));


# ========================================================
# Number of Hash Tables on the metrics
# ========================================================
n = 13;
x = 4:1:n;
numOfHashFunctions = 6;

y1 = IBKNNRecommenderPrecision;
y2 = UBKNNRecommenderPrecision;
y3 = IBLSH1RecommenderHashTablesPrecision;
y4 = IBLSH2RecommenderHashTablesPrecision;
y5 = UBLSH1RecommenderHashTablesPrecision;
y6 = UBLSH2RecommenderHashTablesPrecision;
f= figure();
plot ([4,n], [y1, y1],'-.k', [4,n], [y2, y2],'--k',x, y3,'-+k', x, y4,'->k', x, y5,'-ok', x, y6,'-<k');
h = legend('IB-TOP-N', 'UB-TOP-N', 'IBR-LSH1', 'IBR-LSH2', 'UBR-LSH1', 'UBR-LSH2', 'location', 'northwest');
set (h, 'fontsize', 10);
legend boxon;
xlabel('Number of Hash Tables');
ylabel('Precision');
xlim([4 13])
write(f, strcat(fileName,'-hash-tables-precision'));

-------------
n= 13;
x = 4:1:n;
numOfHashFunctions = 6;

y1 = IBKNNRecommenderDiversity;
y2 = UBKNNRecommenderDiversity;
y3 = IBLSH1RecommenderHashTablesDiversity;
y4 = IBLSH2RecommenderHashTablesDiversity;
y5 = UBLSH1RecommenderHashTablesDiversity;
y6 = UBLSH2RecommenderHashTablesDiversity;
f= figure();
plot ([4,n], [y1, y1],'-.k', [4,n], [y2, y2],'--k',x, y3,'-+k', x, y4,'->k', x, y5,'-ok', x, y6,'-<k');
h = legend('IB-TOP-N', 'UB-TOP-N', 'IBR-LSH1', 'IBR-LSH2', 'UBR-LSH1', 'UBR-LSH2', 'location', 'northwest');
set (h, 'fontsize', 10);
legend boxon;
xlabel('Number of Hash Tables');
ylabel('Diversity');
xlim([4 13])
ylim([0.2 1.35])
write(f, strcat(fileName,'-hash-tables-diversity'));

-------------
n= 13;
x = 4:1:n;
numOfHashFunctions = 6;

y1 = IBKNNRecommenderAggrDiversity;
y2 = UBKNNRecommenderAggrDiversity;
y3 = IBLSH1RecommenderHashTablesAggrDiversity;
y4 = IBLSH2RecommenderHashTablesAggrDiversity;
y5 = UBLSH1RecommenderHashTablesAggrDiversity;
y6 = UBLSH2RecommenderHashTablesAggrDiversity;
f= figure();
plot ([4,n], [y1, y1],'-.k', [4,n], [y2, y2],'--k',x, y3,'-+k', x, y4,'->k', x, y5,'-ok', x, y6,'-<k');
h = legend('IB-TOP-N', 'UB-TOP-N', 'IBR-LSH1', 'IBR-LSH2', 'UBR-LSH1', 'UBR-LSH2', 'location', 'northwest');
set (h, 'fontsize', 10);
legend boxon;
xlabel('Number of Hash Tables');
ylabel('Aggregate Diversity');
xlim([4 13])
write(f, strcat(fileName,'-hash-tables-aggrdiversity'));

-------------

n= 13;
x = 4:1:n;
numOfHashFunctions = 6;

y1 = IBKNNRecommenderNovelty;
y2 = UBKNNRecommenderNovelty;
y3 = IBLSH1RecommenderHashTablesNovelty;
y4 = IBLSH2RecommenderHashTablesNovelty;
y5 = UBLSH1RecommenderHashTablesNovelty;
y6 = UBLSH2RecommenderHashTablesNovelty;
f= figure();
plot ([4,n], [y1, y1],'-.k', [4,n], [y2, y2],'--k',x, y3,'-+k', x, y4,'->k', x, y5,'-ok', x, y6,'-<k');
h = legend('IB-TOP-N', 'UB-TOP-N', 'IBR-LSH1', 'IBR-LSH2', 'UBR-LSH1', 'UBR-LSH2', 'location', 'northwest');
set (h, 'fontsize', 10);
legend boxon;
xlabel('Number of Hash Tables');
ylabel('Novelty');
xlim([4 13])
#ylim([5 8])
write(f, strcat(fileName,'-hash-tables-novelty'));

-------------

y3 = IBLSH1RecommenderHashTablesAvgRecommTime;
y4 = IBLSH2RecommenderHashTablesAvgRecommTime;
y5 = UBLSH1RecommenderHashTablesAvgRecommTime;
y6 = UBLSH2RecommenderHashTablesAvgRecommTime;
f= figure();
plot (x, y3,'-+k', x, y4,'->k', x, y5,'-ok', x, y6,'-<k');
h = legend( 'IBR-LSH1', 'IBR-LSH2', 'UBR-LSH1', 'UBR-LSH2', 'location', 'northwest');
set (h, 'fontsize', 10);
legend boxon;
xlabel('Number of Hash Tables');
ylabel('Avg Recc. Time (ms.)');
xlim([4 13])
write(f, strcat(fileName,'-hash-tables-topn-runtime2'));

f= figure();
plot (x, y3,'-+k', x, y4,'->k');
h = legend( 'IBR-LSH1', 'IBR-LSH2', 'location', 'northwest');
set (h, 'fontsize', 10);
legend boxon;
xlabel('Number of Hash Tables');
ylabel('Avg Recc. Time (ms.)');
xlim([4 13])
write(f, strcat(fileName,'-hash-tables-topn-runtime2-ib'));

f= figure();
plot (x, y5,'-ok', x, y6,'-<k');
h = legend('UBR-LSH1', 'UBR-LSH2', 'location', 'northwest');
set (h, 'fontsize', 10);
legend boxon;
xlabel('Number of Hash Tables');
ylabel('Avg Recc. Time (ms.)');
xlim([4 13])
write(f, strcat(fileName,'-hash-tables-topn-runtime2-ub'));



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
legend("UB","UB-LSH", "location", "northwest");
xlabel("users(x1000)")
ylabel("Model Build Time (sec)");
write(f, strcat(fileName,'-model-build-time-ub'));

f= figure();
plot (x2, y2, '-dk', x2, y4, '--*k' );
legend("IB", "IB-LSH", "location", "northwest");
xlabel("items(x1000)")
ylabel("Model Build Time(sec)");
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
xlabel("Hash Functions");
ylabel("Hash Tables");
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
xlabel("Hash Functions");
ylabel("Hash Tables");
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
xlabel("Hash Functions");
ylabel("Hash Tables");
title("Candidate Set Size (K)");
file = strcat(fileName, '-ublsh-heat-map-candidate');
write(f, file);
