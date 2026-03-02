export interface Product {
    id?: number;
    name: string;
    unitPrice: number;
    description?: string;
    img?: string; // base64

    byteImg?: string; // base64
    category?: string;
}
