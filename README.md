# Concurrent Editor

The document is the current text with a list of modifications. Changes are position(cursor) offset. When a user submits a change on the server, the user’s number of version is compared to the current number of version. If they are different the position is shifted according to the list of modifications. After that the text is changed(add or delete). After a change in a separate thread all users are updated. All text changes are executed with synchronization since the changes are applied to the shared resource.
