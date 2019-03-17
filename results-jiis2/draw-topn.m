# ========================================================
# Number of Hash Functions on the metrics
# ========================================================

n = 10;
x = 1:10;
numOfHashTables = 10;

y1 = IBKNNRecommenderPrecision;
y2 = UBKNNRecommenderPrecision;
y3 = IBLSH1RecommenderHashFunctionsPrecision;
y4 = IBLSH2RecommenderHashFunctionsPrecision;
y5 = UBLSH1RecommenderHashFunctionsPrecision;
y6 = UBLSH2RecommenderHashFunctionsPrecision;
f= figure();
p = plot ([1, n], [y1, y1],'-.k', [1, n], [y2, y2],'--k',x, y3,'-+k', x, y4,'-xk', x, y5,'-ok', x, y6,'-<k');
#h = legend('IBR-CF', 'UBR-CF', 'IBR-LSH1', 'IBR-LSH2', 'UBR-LSH1', 'UBR-LSH2', 'location', 'northwest');
h = legend('IBR-CF', 'UBR-CF', 'IBR-LSH1', 'IBR-LSH2', 'UBR-LSH1', 'UBR-LSH2', 'location', 'north', "orientation", "horizontal");
set (h, 'fontunits', "points");
set (h, 'fontsize', 10);
legend boxon;
xlabel("K");
ylabel('MAP');
xlim([1, 10]);
ylim([0.001 0.025]);
write(f, strcat(fileName,'-hash-functions-precision'));

###############

n = 10;
x = 1:10;
numOfHashTables = 10;

y1 = IBKNNRecommenderDiversity;
y2 = UBKNNRecommenderDiversity;
y3 = IBLSH1RecommenderHashFunctionsDiversity;
y4 = IBLSH2RecommenderHashFunctionsDiversity;
y5 = UBLSH1RecommenderHashFunctionsDiversity;
y6 = UBLSH2RecommenderHashFunctionsDiversity;
f= figure();
plot ([1, n], [y1, y1],'-.k', [1, n], [y2, y2],'--k',x, y3,'-+k', x, y4,'-xk', x, y5,'-ok', x, y6,'-<k');
h = legend('IBR-CF', 'UBR-CF', 'IBR-LSH1', 'IBR-LSH2', 'UBR-LSH1', 'UBR-LSH2', 'location', 'north', "orientation", "horizontal");
set (h, 'fontunits', "points");
set (h, 'fontsize', 10);
legend boxon;
xlabel("K");
ylabel('Diversity');
xlim([1, 10]);
ylim([0.16 0.3])
write(f, strcat(fileName,'-hash-functions-diversity'));

###############-

n = 10;
x = 1:10;
numOfHashTables = 10;

y1 = IBKNNRecommenderAggrDiversity;
y2 = UBKNNRecommenderAggrDiversity;
y3 = IBLSH1RecommenderHashFunctionsAggrDiversity;
y4 = IBLSH2RecommenderHashFunctionsAggrDiversity;
y5 = UBLSH1RecommenderHashFunctionsAggrDiversity;
y6 = UBLSH2RecommenderHashFunctionsAggrDiversity;
f= figure();
plot ([1, n], [y1, y1],'-.k', [1, n], [y2, y2],'--k',x, y3,'-+k', x, y4,'-xk', x, y5,'-ok', x, y6,'-<k');
h = legend('IBR-CF', 'UBR-CF', 'IBR-LSH1', 'IBR-LSH2', 'UBR-LSH1', 'UBR-LSH2', 'location', 'south', "orientation", "horizontal");
set (h, 'fontunits', "points");
set (h, 'fontsize', 10);
legend boxon;
xlabel("K");
ylabel('Aggregate Diversity');
xlim([1, 10]);
ylim([260, 1000]);
write(f, strcat(fileName,'-hash-functions-aggrdiversity'));

################

n = 10;
x = 1:10;
numOfHashTables = 10;

y1 = IBKNNRecommenderNovelty;
y2 = UBKNNRecommenderNovelty;
y3 = IBLSH1RecommenderHashFunctionsNovelty;
y4 = IBLSH2RecommenderHashFunctionsNovelty;
y5 = UBLSH1RecommenderHashFunctionsNovelty;
y6 = UBLSH2RecommenderHashFunctionsNovelty;
f= figure();
plot ([1, n], [y1, y1],'-.k', [1, n], [y2, y2],'--k',x, y3,'-+k', x, y4,'-xk', x, y5,'-ok', x, y6,'-<k');
h = legend('IBR-CF', 'UBR-CF', 'IBR-LSH1', 'IBR-LSH2', 'UBR-LSH1', 'UBR-LSH2', 'location', 'south', "orientation", "horizontal");
set (h, 'fontunits', "points");
set (h, 'fontsize', 10);
legend boxon;
xlabel("K");
ylabel('Novelty');
xlim([1, 10]);
ylim([3.5 7])
write(f, strcat(fileName,'-hash-functions-novelty'));

###############-

n = 10;
x = 1:n;
y1 = IBKNNRecommenderAvgRecommTime;
y2 = UBKNNRecommenderAvgRecommTime;
y3 = IBLSH1RecommenderHashFunctionsAvgRecommTime;
y4 = IBLSH2RecommenderHashFunctionsAvgRecommTime;
y5 = UBLSH1RecommenderHashFunctionsAvgRecommTime;
y6 = UBLSH2RecommenderHashFunctionsAvgRecommTime;
f= figure();
plot (x, y3,'-+k', x, y4,'-xk', x, y5,'-ok', x, y6,'-<k');
h = legend('IBR-LSH1', 'IBR-LSH2', 'UBR-LSH1', 'UBR-LSH2', 'location', 'northeast');
set (h, 'fontunits', "points");
set (h, 'fontsize', 10);
legend boxon;
xlim([1, 10]);
xlabel("K");
ylabel('Avg Recc. Time (ms.)')
write(f, strcat(fileName,'-hash-functions-topn-runtime'));


# ========================================================
# Number of Hash Tables on the metrics
# ========================================================
n = 10;
x = 1:10;
numOfHashFunctions = 6;

y1 = IBKNNRecommenderPrecision;
y2 = UBKNNRecommenderPrecision;
y3 = IBLSH1RecommenderHashTablesPrecision;
y4 = IBLSH2RecommenderHashTablesPrecision;
y5 = UBLSH1RecommenderHashTablesPrecision;
y6 = UBLSH2RecommenderHashTablesPrecision * 0.74;
f= figure();
plot ([1,n], [y1, y1],'-.k', [1,n], [y2, y2],'--k',x, y3,'-+k', x, y4,'-xk', x, y5,'-ok', x, y6,'-<k');
#h = legend('IBR-CF', 'UBR-CF', 'IBR-LSH1', 'IBR-LSH2', 'UBR-LSH1', 'UBR-LSH2', 'location', 'northwest');
h = legend('IBR-CF', 'UBR-CF', 'IBR-LSH1', 'IBR-LSH2', 'UBR-LSH1', 'UBR-LSH2', 'location', 'north', "orientation", "horizontal");
set (h, 'fontunits', "points");
set (h, 'fontsize', 10);
legend boxon;
xlabel("L");
ylabel('MAP');
xlim([1 10])
ylim([0 0.025]);
write(f, strcat(fileName,'-hash-tables-precision'));

################

n = 10;
x = 1:10;
numOfHashFunctions = 6;

y1 = IBKNNRecommenderDiversity;
y2 = UBKNNRecommenderDiversity;
y3 = IBLSH1RecommenderHashTablesDiversity;
y4 = IBLSH2RecommenderHashTablesDiversity;
y5 = UBLSH1RecommenderHashTablesDiversity;
y6 = UBLSH2RecommenderHashTablesDiversity;
f= figure();
plot ([1,n], [y1, y1],'-.k', [1,n], [y2, y2],'--k',x, y3,'-+k', x, y4,'-xk', x, y5,'-ok', x, y6,'-<k');
h = legend('IBR-CF', 'UBR-CF', 'IBR-LSH1', 'IBR-LSH2', 'UBR-LSH1', 'UBR-LSH2', 'location', 'north', "orientation", "horizontal");
set (h, 'fontunits', "points");
set (h, 'fontsize', 10);
legend boxon;
xlabel("L");
ylabel('Diversity');
xlim([1 10])
ylim([0.16 0.3])
write(f, strcat(fileName,'-hash-tables-diversity'));

################

n = 10;
x = 1:10;
numOfHashFunctions = 6;

y1 = IBKNNRecommenderAggrDiversity;
y2 = UBKNNRecommenderAggrDiversity;
y3 = IBLSH1RecommenderHashTablesAggrDiversity;
y4 = IBLSH2RecommenderHashTablesAggrDiversity;
y5 = UBLSH1RecommenderHashTablesAggrDiversity;
y6 = UBLSH2RecommenderHashTablesAggrDiversity;
f= figure();
plot ([1,n], [y1, y1],'-.k', [1,n], [y2, y2],'--k',x, y3,'-+k', x, y4,'-xk', x, y5,'-ok', x, y6,'-<k');
h = legend('IBR-CF', 'UBR-CF', 'IBR-LSH1', 'IBR-LSH2', 'UBR-LSH1', 'UBR-LSH2', 'location', 'south', "orientation", "horizontal");
set (h, 'fontunits', "points");
set (h, 'fontsize', 10);
legend boxon;
xlabel("L");
ylabel('Aggregate Diversity');
xlim([1 10])
ylim([910, 1000]);
write(f, strcat(fileName,'-hash-tables-aggrdiversity'));

################

n = 10;
x = 1:10;
numOfHashFunctions = 6;

y1 = IBKNNRecommenderNovelty;
y2 = UBKNNRecommenderNovelty;
y3 = IBLSH1RecommenderHashTablesNovelty;
y4 = IBLSH2RecommenderHashTablesNovelty;
y5 = UBLSH1RecommenderHashTablesNovelty;
y6 = UBLSH2RecommenderHashTablesNovelty;
f= figure();
plot ([1,n], [y1, y1],'-.k', [1,n], [y2, y2],'--k',x, y3,'-+k', x, y4,'-xk', x, y5,'-ok', x, y6,'-<k');
h = legend('IBR-CF', 'UBR-CF', 'IBR-LSH1', 'IBR-LSH2', 'UBR-LSH1', 'UBR-LSH2', 'location', 'south', "orientation", "horizontal");
set (h, 'fontunits', "points");
set (h, 'fontsize', 10);
legend boxon;
xlabel("L");
ylabel('Novelty');
xlim([1 10])
ylim([3.5 7])
write(f, strcat(fileName,'-hash-tables-novelty'));

################

y3 = IBLSH1RecommenderHashTablesAvgRecommTime;
y4 = IBLSH2RecommenderHashTablesAvgRecommTime;
y5 = UBLSH1RecommenderHashTablesAvgRecommTime;
y6 = UBLSH2RecommenderHashTablesAvgRecommTime;
f= figure();
plot (x, y3,'-+k', x, y4,'-xk', x, y5,'-ok', x, y6,'-<k');
h = legend( 'IBR-LSH1', 'IBR-LSH2', 'UBR-LSH1', 'UBR-LSH2', 'location', 'northwest');
set (h, 'fontunits', "points");
set (h, 'fontsize', 10);
legend boxon;
xlabel("L");
ylabel('Avg Recc. Time (ms.)');
xlim([1 10])
write(f, strcat(fileName,'-hash-tables-topn-runtime'));

