import tensorflow as tf

# Input my own data.
filenames = [('/data/%d.jpg' % i) for i in range(50)]
filename_queue = tf.train.string_input_producer(filenames)
reader = tf.WholeFileReader()
filename, content = reader.read(filename_queue)
image = tf.image.decode_jpeg(content, channels = 3)
image = tf.cast(image, tf.float32)
resized_image = tf.image.resize_images(image, [32, 32])
input = tf.train.batch([resized_image], batch_size = 8)
fp = open('data/label.txt', 'r')
labels = fp.readlines()
for i in labels:
    i = int(i)
fp.close()

sess = tf.Session()
tf.logging.set_verbosity(tf.logging.INFO)

x = tf.placeholder(tf.float32, [None, 1024], name = 'x')
W = tf.Variable(tf.zeros([1024, 3]), name = 'W')
b = tf.Variable(tf.zeros([3]),name = 'b')

y = tf.nn.softmax(tf.matmul(x, W) + b, name = 'y')
y_ = tf.placeholder(tf.float32, [None, 3], name = 'y_')
tf.add_to_collection('variables', W)
tf.add_to_collection('variables', b)

cross_entropy = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(logits = y, labels = y_))

train_step = tf.train.GradientDescentOptimizer(0.5).minimize(cross_entropy)

# save summaries for visualization
tf.summary.histogram('weights', W)
tf.summary.histogram('max_weight', tf.reduce_max(W))
tf.summary.histogram('bias', b)
tf.summary.scalar('cross_entropy', cross_entropy)
tf.summary.histogram('cross_hist', cross_entropy)

# merge all summaries into one op
merged=tf.summary.merge_all()

trainwriter=tf.summary.FileWriter('model/training',sess.graph)

init = tf.global_variables_initializer()
sess.run(init)

for i in range(0):
    with sess.as_default():
        batch_xs, batch_ys = input.eval(), labels
    summary, _ = sess.run([merged, train_step], feed_dict = {x: batch_xs, y_: batch_ys})
    trainwriter.add_summary(summary, i)

# model export path
export_path = 'model/training'
print('Exporting trained model to', export_path)
