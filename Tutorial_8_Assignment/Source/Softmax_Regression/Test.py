import tensorflow as tf
tf.logging.set_verbosity(tf.logging.DEBUG)
sess = tf.Session()

# import mnist data
filenames = [('/data/%d.jpg' % i) for i in range(50, 60)]
filename_queue = tf.train.string_input_producer(filenames)
reader = tf.WholeFileReader()
filename, content = reader.read(filename_queue)
image = tf.image.decode_jpeg(content, channels = 3)
image = tf.cast(image, tf.float32)
resized_image = tf.image.resize_images(image, [32, 32])
input = tf.train.batch([resized_image], batch_size = 8)

# restore the saved model
new_saver = tf.train.import_meta_graph('model/training/events.out.tfevents.1489548974.SUGAR-V')
new_saver.restore(sess, 'model/training/events.out.tfevents.1489548974.SUGAR-V')

# print to see the restored variables
for v in tf.get_collection('variables'):
    print(v.name)
print(sess.run(tf.global_variables()))

# get saved weights
W = tf.get_collection('variables')[0]
b = tf.get_collection('variables')[1]

# placeholders for test images and labels
x = tf.placeholder(tf.float32, [None, 784],name='x')
y_ = tf.placeholder(tf.float32, [None, 10],name='y_')

# predict equation
y = tf.nn.softmax(tf.matmul(x, W) + b,name='y')

# compare predicted label and actual label
correct_prediction = tf.equal(tf.argmax(y,1), tf.argmax(y_,1))

# accuracy op
accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))

accu=sess.run(accuracy, feed_dict={x: mnist.test.images, y_: mnist.test.labels})
print(accu)
