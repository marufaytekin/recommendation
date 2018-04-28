import os
import random


dirs = filter(os.path.isdir, os.listdir('.'))
file_size_dic = {}

for dir in dirs:
    files = os.listdir(dir)
    files = [file for file in files if file.endswith('.base') or file.endswith('.test') or file.endswith('.val')]
    file_lines = []
    for file in files:
        f = open(os.path.join(dir, file), 'r')
        lines  = f.readlines()
        print(file + ":" + str(len(lines)))
        f.close()
        file_lines = file_lines + lines
    file_size_dic[dir] = len(file_lines)
    print("all data size:", len(file_lines))
    f = open(os.path.join(dir, dir + '.dat'), 'w')
    random.shuffle(file_lines)
    f.writelines(file_lines)

# testing files
for dir in dirs:
    f = open(os.path.join(dir, dir + '.dat'), 'r')
    lines = f.readlines()
    try:
        assert file_size_dic[dir] == len(lines)
    except AssertionError:
        print(file_size_dic[dir], len (lines))


def chunks(l, n):
    """ Yield n successive chunks from l.
    """
    newn = int(len(l) / n)
    for i in xrange(0, n-1):
        yield l[i * newn: i * newn + newn]
    yield l[n * newn - newn:]


#prepare for CV
for dir in dirs:
    f = open(os.path.join(dir, dir + '.dat'), 'r')
    lines = f.readlines()
    ten_chunks = chunks(lines, 10)
    for chunk in range(10):
        f = open(os.path.join(dir, dir + str(chunk)), 'w')
        f.writelines(ten_chunks.next())
        f.close()
