namespace test
{
    partial class Form1
    {
        /// <summary>
        /// 必需的设计器变量。
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// 清理所有正在使用的资源。
        /// </summary>
        /// <param name="disposing">如果应释放托管资源，为 true；否则为 false。</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows 窗体设计器生成的代码

        /// <summary>
        /// 设计器支持所需的方法 - 不要
        /// 使用代码编辑器修改此方法的内容。
        /// </summary>
        private void InitializeComponent()
        {
            this.richTextBox_code = new System.Windows.Forms.RichTextBox();
            this.listBox_code = new System.Windows.Forms.ListBox();
            this.button_open = new System.Windows.Forms.Button();
            this.splitContainer_GlobalLayout = new System.Windows.Forms.SplitContainer();
            this.button_save_Result = new System.Windows.Forms.Button();
            this.button_test = new System.Windows.Forms.Button();
            this.button_saveTmpFile = new System.Windows.Forms.Button();
            this.button_split_text = new System.Windows.Forms.Button();
            this.splitContainer_bottomlayout = new System.Windows.Forms.SplitContainer();
            this.button1_test2 = new System.Windows.Forms.Button();
            ((System.ComponentModel.ISupportInitialize)(this.splitContainer_GlobalLayout)).BeginInit();
            this.splitContainer_GlobalLayout.Panel1.SuspendLayout();
            this.splitContainer_GlobalLayout.Panel2.SuspendLayout();
            this.splitContainer_GlobalLayout.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.splitContainer_bottomlayout)).BeginInit();
            this.splitContainer_bottomlayout.Panel1.SuspendLayout();
            this.splitContainer_bottomlayout.Panel2.SuspendLayout();
            this.splitContainer_bottomlayout.SuspendLayout();
            this.SuspendLayout();
            // 
            // richTextBox_code
            // 
            this.richTextBox_code.Dock = System.Windows.Forms.DockStyle.Fill;
            this.richTextBox_code.Location = new System.Drawing.Point(0, 0);
            this.richTextBox_code.Name = "richTextBox_code";
            this.richTextBox_code.Size = new System.Drawing.Size(276, 356);
            this.richTextBox_code.TabIndex = 0;
            this.richTextBox_code.Text = "";
            this.richTextBox_code.KeyDown += new System.Windows.Forms.KeyEventHandler(this.richTextBox_code_KeyDown);
            // 
            // listBox_code
            // 
            this.listBox_code.Dock = System.Windows.Forms.DockStyle.Fill;
            this.listBox_code.FormattingEnabled = true;
            this.listBox_code.ItemHeight = 12;
            this.listBox_code.Location = new System.Drawing.Point(0, 0);
            this.listBox_code.Name = "listBox_code";
            this.listBox_code.Size = new System.Drawing.Size(548, 356);
            this.listBox_code.TabIndex = 1;
            this.listBox_code.DoubleClick += new System.EventHandler(this.listBox_code_DoubleClick);
            // 
            // button_open
            // 
            this.button_open.Location = new System.Drawing.Point(12, 12);
            this.button_open.Name = "button_open";
            this.button_open.Size = new System.Drawing.Size(75, 23);
            this.button_open.TabIndex = 2;
            this.button_open.Text = "Open";
            this.button_open.UseVisualStyleBackColor = true;
            this.button_open.Click += new System.EventHandler(this.button_open_Click);
            // 
            // splitContainer_GlobalLayout
            // 
            this.splitContainer_GlobalLayout.Dock = System.Windows.Forms.DockStyle.Fill;
            this.splitContainer_GlobalLayout.FixedPanel = System.Windows.Forms.FixedPanel.Panel1;
            this.splitContainer_GlobalLayout.Location = new System.Drawing.Point(0, 0);
            this.splitContainer_GlobalLayout.Name = "splitContainer_GlobalLayout";
            this.splitContainer_GlobalLayout.Orientation = System.Windows.Forms.Orientation.Horizontal;
            // 
            // splitContainer_GlobalLayout.Panel1
            // 
            this.splitContainer_GlobalLayout.Panel1.Controls.Add(this.button1_test2);
            this.splitContainer_GlobalLayout.Panel1.Controls.Add(this.button_save_Result);
            this.splitContainer_GlobalLayout.Panel1.Controls.Add(this.button_test);
            this.splitContainer_GlobalLayout.Panel1.Controls.Add(this.button_saveTmpFile);
            this.splitContainer_GlobalLayout.Panel1.Controls.Add(this.button_split_text);
            this.splitContainer_GlobalLayout.Panel1.Controls.Add(this.button_open);
            // 
            // splitContainer_GlobalLayout.Panel2
            // 
            this.splitContainer_GlobalLayout.Panel2.Controls.Add(this.splitContainer_bottomlayout);
            this.splitContainer_GlobalLayout.Size = new System.Drawing.Size(828, 403);
            this.splitContainer_GlobalLayout.SplitterDistance = 43;
            this.splitContainer_GlobalLayout.TabIndex = 3;
            // 
            // button_save_Result
            // 
            this.button_save_Result.Location = new System.Drawing.Point(337, 11);
            this.button_save_Result.Name = "button_save_Result";
            this.button_save_Result.Size = new System.Drawing.Size(89, 23);
            this.button_save_Result.TabIndex = 6;
            this.button_save_Result.Text = "Save分词结果";
            this.button_save_Result.UseVisualStyleBackColor = true;
            this.button_save_Result.Click += new System.EventHandler(this.button_save_Result_Click);
            // 
            // button_test
            // 
            this.button_test.Location = new System.Drawing.Point(255, 12);
            this.button_test.Name = "button_test";
            this.button_test.Size = new System.Drawing.Size(75, 23);
            this.button_test.TabIndex = 5;
            this.button_test.Text = "Test分词";
            this.button_test.UseVisualStyleBackColor = true;
            this.button_test.Click += new System.EventHandler(this.button_test_Click);
            // 
            // button_saveTmpFile
            // 
            this.button_saveTmpFile.Location = new System.Drawing.Point(93, 12);
            this.button_saveTmpFile.Name = "button_saveTmpFile";
            this.button_saveTmpFile.Size = new System.Drawing.Size(75, 23);
            this.button_saveTmpFile.TabIndex = 4;
            this.button_saveTmpFile.Text = "Save";
            this.button_saveTmpFile.UseVisualStyleBackColor = true;
            this.button_saveTmpFile.Click += new System.EventHandler(this.button_saveTmpFile_Click);
            // 
            // button_split_text
            // 
            this.button_split_text.Location = new System.Drawing.Point(174, 12);
            this.button_split_text.Name = "button_split_text";
            this.button_split_text.Size = new System.Drawing.Size(75, 23);
            this.button_split_text.TabIndex = 3;
            this.button_split_text.Text = "Split";
            this.button_split_text.UseVisualStyleBackColor = true;
            this.button_split_text.Click += new System.EventHandler(this.button_split_text_Click);
            // 
            // splitContainer_bottomlayout
            // 
            this.splitContainer_bottomlayout.Dock = System.Windows.Forms.DockStyle.Fill;
            this.splitContainer_bottomlayout.Location = new System.Drawing.Point(0, 0);
            this.splitContainer_bottomlayout.Name = "splitContainer_bottomlayout";
            // 
            // splitContainer_bottomlayout.Panel1
            // 
            this.splitContainer_bottomlayout.Panel1.Controls.Add(this.richTextBox_code);
            // 
            // splitContainer_bottomlayout.Panel2
            // 
            this.splitContainer_bottomlayout.Panel2.Controls.Add(this.listBox_code);
            this.splitContainer_bottomlayout.Size = new System.Drawing.Size(828, 356);
            this.splitContainer_bottomlayout.SplitterDistance = 276;
            this.splitContainer_bottomlayout.TabIndex = 0;
            // 
            // button1_test2
            // 
            this.button1_test2.Location = new System.Drawing.Point(432, 11);
            this.button1_test2.Name = "button1_test2";
            this.button1_test2.Size = new System.Drawing.Size(75, 23);
            this.button1_test2.TabIndex = 7;
            this.button1_test2.Text = "Test2";
            this.button1_test2.UseVisualStyleBackColor = true;
            this.button1_test2.Click += new System.EventHandler(this.button1_test2_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(828, 403);
            this.Controls.Add(this.splitContainer_GlobalLayout);
            this.KeyPreview = true;
            this.Name = "Form1";
            this.Text = "Form1";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.Form1_FormClosing);
            this.Load += new System.EventHandler(this.Form1_Load);
            this.KeyDown += new System.Windows.Forms.KeyEventHandler(this.Form1_KeyDown);
            this.splitContainer_GlobalLayout.Panel1.ResumeLayout(false);
            this.splitContainer_GlobalLayout.Panel2.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.splitContainer_GlobalLayout)).EndInit();
            this.splitContainer_GlobalLayout.ResumeLayout(false);
            this.splitContainer_bottomlayout.Panel1.ResumeLayout(false);
            this.splitContainer_bottomlayout.Panel2.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.splitContainer_bottomlayout)).EndInit();
            this.splitContainer_bottomlayout.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.RichTextBox richTextBox_code;
        private System.Windows.Forms.ListBox listBox_code;
        private System.Windows.Forms.Button button_open;
        private System.Windows.Forms.SplitContainer splitContainer_GlobalLayout;
        private System.Windows.Forms.SplitContainer splitContainer_bottomlayout;
        private System.Windows.Forms.Button button_split_text;
        private System.Windows.Forms.Button button_saveTmpFile;
        private System.Windows.Forms.Button button_test;
        private System.Windows.Forms.Button button_save_Result;
        private System.Windows.Forms.Button button1_test2;
    }
}

