document.addEventListener('DOMContentLoaded', function() {
    // Search item by ID
    document.getElementById('searchItem').addEventListener('click', function() {
        const itemId = document.getElementById('itemId').value.trim();
        console.log('Searching for item:', itemId);

        if (itemId) {
            fetch(`bill-item?itemId=${itemId}`)
                .then(response => {
                    console.log('Response status:', response.status); // Debug log
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    console.log('Received data:', data); // Debug log
                    if (data.error) {
                        alert(data.error);
                        document.getElementById('itemDetails').style.display = 'none';
                    } else {
                        document.getElementById('itemName').textContent = data.name;
                        document.getElementById('itemPrice').textContent = 'Rs. ' + data.unitPrice.toFixed(2);
                        document.getElementById('itemStock').textContent = data.stock;
                        document.getElementById('quantity').max = data.stock;
                        document.getElementById('quantity').value = 1;
                        document.getElementById('itemDetails').style.display = 'block';
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Error fetching item details');
                });
        }   else {
            alert('Please enter an Item ID');
        }
    });

    // Add item to bill
    document.getElementById('addItem').addEventListener('click', function() {
        const itemId = document.getElementById('itemId').value.trim();
        const quantity = parseInt(document.getElementById('quantity').value);

        if (itemId && quantity > 0) {
            const formData = new FormData();
            formData.append('action', 'addItem');
            formData.append('itemId', itemId);
            formData.append('quantity', quantity);

            fetch('billing', {
                method: 'POST',
                body: formData
            })
                .then(response => {
                    if (response.redirected) {
                        window.location.href = response.url;
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Error adding item to bill');
                });
        }
    });

    // Remove item from bill
    document.addEventListener('click', function(e) {
        if (e.target.classList.contains('remove-item')) {
            const index = e.target.getAttribute('data-index');
            fetch('billing', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `action=removeItem&index=${index}`
            })
                .then(response => {
                    if (response.redirected) {
                        window.location.href = response.url;
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Error removing item from bill');
                });
        }
    });

    // Calculate totals (for display only - actual calculation happens on server)
    function calculateTotals() {
        let subtotal = 0;
        const rows = document.querySelectorAll('#billItemsTable tbody tr');

        rows.forEach(row => {
            const price = parseFloat(row.cells[2].textContent);
            const qty = parseInt(row.cells[3].textContent);
            subtotal += price * qty;
        });

        // Simplified calculation for display
        // Actual discount would be calculated based on customer type
        const discount = subtotal * 0.10; // Example 10% discount
        const tax = (subtotal - discount) * 0.15; // 15% tax

        document.getElementById('subtotal').textContent = subtotal.toFixed(2);
        document.getElementById('discount').textContent = discount.toFixed(2);
        document.getElementById('tax').textContent = tax.toFixed(2);
        document.getElementById('total').textContent = (subtotal - discount + tax).toFixed(2);
    }

    // Initialize totals calculation
    calculateTotals();
});