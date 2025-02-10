document.addEventListener('DOMContentLoaded', function() {
    // Initialize
    loadProducts();
    loadCategories();

    // Form submissions
    document.getElementById('uploadForm').addEventListener('submit', handleFileUpload);
    document.getElementById('productForm').addEventListener('submit', handleProductSubmit);
    
    // Search and filter
    document.getElementById('searchInput').addEventListener('input', handleSearch);
    document.getElementById('categoryFilter').addEventListener('change', handleCategoryFilter);
});

async function loadProducts() {
    try {
        const response = await fetch('/api/products');
        const products = await response.json();
        displayProducts(products);
    } catch (error) {
        console.error('Error loading products:', error);
    }
}

async function loadCategories() {
    try {
        const response = await fetch('/api/products');
        const products = await response.json();
        const categories = [...new Set(products.map(p => p.category))];
        const select = document.getElementById('categoryFilter');
        categories.forEach(category => {
            const option = document.createElement('option');
            option.value = category;
            option.textContent = category;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Error loading categories:', error);
    }
}

async function handleFileUpload(e) {
    e.preventDefault();
    const fileInput = document.getElementById('fileInput');
    const file = fileInput.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append('file', file);

    try {
        const response = await fetch('/api/oss/upload', {
            method: 'POST',
            body: formData
        });
        const result = await response.json();
        showUploadStatus('File uploaded successfully!', 'success');
    } catch (error) {
        showUploadStatus('Error uploading file', 'error');
    }
}

async function handleProductSubmit(e) {
    e.preventDefault();
    const product = {
        name: document.getElementById('productName').value,
        description: document.getElementById('description').value,
        price: parseFloat(document.getElementById('price').value),
        stockQuantity: parseInt(document.getElementById('stockQuantity').value),
        category: document.getElementById('category').value
    };

    try {
        const response = await fetch('/api/products', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(product)
        });
        await loadProducts();
        e.target.reset();
    } catch (error) {
        console.error('Error creating product:', error);
    }
}

function displayProducts(products) {
    const tbody = document.getElementById('productList');
    tbody.innerHTML = '';
    
    products.forEach(product => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${product.name}</td>
            <td>${product.description || '-'}</td>
            <td>$${product.price.toFixed(2)}</td>
            <td>${product.stockQuantity}</td>
            <td>${product.category}</td>
            <td>
                <button onclick="deleteProduct(${product.id})">Delete</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

async function deleteProduct(id) {
    if (!confirm('Are you sure you want to delete this product?')) return;
    
    try {
        await fetch(`/api/products/${id}`, {
            method: 'DELETE'
        });
        await loadProducts();
    } catch (error) {
        console.error('Error deleting product:', error);
    }
}

function showUploadStatus(message, type) {
    const status = document.getElementById('uploadStatus');
    status.textContent = message;
    status.className = type;
}

async function handleSearch(e) {
    const searchTerm = e.target.value;
    if (!searchTerm) {
        await loadProducts();
        return;
    }
    
    try {
        const response = await fetch(`/api/products/search?name=${searchTerm}`);
        const products = await response.json();
        displayProducts(products);
    } catch (error) {
        console.error('Error searching products:', error);
    }
}

async function handleCategoryFilter(e) {
    const category = e.target.value;
    if (!category) {
        await loadProducts();
        return;
    }
    
    try {
        const response = await fetch(`/api/products/category/${category}`);
        const products = await response.json();
        displayProducts(products);
    } catch (error) {
        console.error('Error filtering products:', error);
    }
}
