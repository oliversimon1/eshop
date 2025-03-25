CREATE TABLE PRODUCTS
(
    id    UUID         NOT NULL PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    stock INT          NOT NULL
);

CREATE TABLE ORDERS
(
    id          UUID        NOT NULL PRIMARY KEY,
    status      VARCHAR(50) NOT NULL,
    price DOUBLE NOT NULL,
    version     INT DEFAULT 0,
    created_at  TIMESTAMP WITH TIME ZONE,
    modified_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE ORDER_ITEMS
(
    id         UUID NOT NULL PRIMARY KEY,
    quantity   INT  NOT NULL,
    product_name  VARCHAR(255) NOT NULL,
    product_price DOUBLE NOT NULL,
    product_id UUID,
    order_id   UUID NOT NULL,
    CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES PRODUCTS (id),
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES ORDERS (id) ON DELETE CASCADE
);
