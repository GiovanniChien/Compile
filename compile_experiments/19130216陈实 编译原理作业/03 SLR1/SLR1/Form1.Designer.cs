namespace SLR1
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
            this.textBox_String = new System.Windows.Forms.TextBox();
            this.button_Ok = new System.Windows.Forms.Button();
            this.textBox_result = new System.Windows.Forms.TextBox();
            this.richTextBox_rules = new System.Windows.Forms.RichTextBox();
            this.button_getrules = new System.Windows.Forms.Button();
            this.listView_showRules = new System.Windows.Forms.ListView();
            this.button_createDFA = new System.Windows.Forms.Button();
            this.button_FillRuleTable = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.button_All = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // textBox_String
            // 
            this.textBox_String.Location = new System.Drawing.Point(272, 148);
            this.textBox_String.Name = "textBox_String";
            this.textBox_String.Size = new System.Drawing.Size(100, 21);
            this.textBox_String.TabIndex = 0;
            // 
            // button_Ok
            // 
            this.button_Ok.Location = new System.Drawing.Point(283, 175);
            this.button_Ok.Name = "button_Ok";
            this.button_Ok.Size = new System.Drawing.Size(75, 23);
            this.button_Ok.TabIndex = 1;
            this.button_Ok.Text = "识别句子";
            this.button_Ok.UseVisualStyleBackColor = true;
            this.button_Ok.Click += new System.EventHandler(this.button_Ok_Click);
            // 
            // textBox_result
            // 
            this.textBox_result.Location = new System.Drawing.Point(272, 204);
            this.textBox_result.Name = "textBox_result";
            this.textBox_result.Size = new System.Drawing.Size(100, 21);
            this.textBox_result.TabIndex = 0;
            // 
            // richTextBox_rules
            // 
            this.richTextBox_rules.Location = new System.Drawing.Point(12, 12);
            this.richTextBox_rules.Name = "richTextBox_rules";
            this.richTextBox_rules.Size = new System.Drawing.Size(144, 244);
            this.richTextBox_rules.TabIndex = 2;
            this.richTextBox_rules.Text = "";
            // 
            // button_getrules
            // 
            this.button_getrules.Location = new System.Drawing.Point(272, 22);
            this.button_getrules.Name = "button_getrules";
            this.button_getrules.Size = new System.Drawing.Size(75, 23);
            this.button_getrules.TabIndex = 3;
            this.button_getrules.Text = "获取规则";
            this.button_getrules.UseVisualStyleBackColor = true;
            this.button_getrules.Click += new System.EventHandler(this.button_getrules_Click);
            // 
            // listView_showRules
            // 
            this.listView_showRules.Location = new System.Drawing.Point(405, 12);
            this.listView_showRules.Name = "listView_showRules";
            this.listView_showRules.Size = new System.Drawing.Size(319, 244);
            this.listView_showRules.TabIndex = 4;
            this.listView_showRules.UseCompatibleStateImageBehavior = false;
            // 
            // button_createDFA
            // 
            this.button_createDFA.Location = new System.Drawing.Point(272, 51);
            this.button_createDFA.Name = "button_createDFA";
            this.button_createDFA.Size = new System.Drawing.Size(75, 23);
            this.button_createDFA.TabIndex = 5;
            this.button_createDFA.Text = "创建自动机";
            this.button_createDFA.UseVisualStyleBackColor = true;
            this.button_createDFA.Click += new System.EventHandler(this.button_createDFA_Click);
            // 
            // button_FillRuleTable
            // 
            this.button_FillRuleTable.Location = new System.Drawing.Point(272, 81);
            this.button_FillRuleTable.Name = "button_FillRuleTable";
            this.button_FillRuleTable.Size = new System.Drawing.Size(75, 23);
            this.button_FillRuleTable.TabIndex = 6;
            this.button_FillRuleTable.Text = "填充分析表";
            this.button_FillRuleTable.UseVisualStyleBackColor = true;
            this.button_FillRuleTable.Click += new System.EventHandler(this.button_FillRuleTable_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(201, 151);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(65, 12);
            this.label1.TabIndex = 7;
            this.label1.Text = "待识别句子";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(201, 207);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(53, 12);
            this.label2.TabIndex = 7;
            this.label2.Text = "识别结果";
            // 
            // button_All
            // 
            this.button_All.Location = new System.Drawing.Point(272, 111);
            this.button_All.Name = "button_All";
            this.button_All.Size = new System.Drawing.Size(75, 23);
            this.button_All.TabIndex = 8;
            this.button_All.Text = "ALL";
            this.button_All.UseVisualStyleBackColor = true;
            this.button_All.Click += new System.EventHandler(this.button_All_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(771, 268);
            this.Controls.Add(this.button_All);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.button_FillRuleTable);
            this.Controls.Add(this.button_createDFA);
            this.Controls.Add(this.listView_showRules);
            this.Controls.Add(this.button_getrules);
            this.Controls.Add(this.richTextBox_rules);
            this.Controls.Add(this.button_Ok);
            this.Controls.Add(this.textBox_result);
            this.Controls.Add(this.textBox_String);
            this.Name = "Form1";
            this.Text = "Form1";
            this.Load += new System.EventHandler(this.Form1_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.TextBox textBox_String;
        private System.Windows.Forms.Button button_Ok;
        private System.Windows.Forms.TextBox textBox_result;
        private System.Windows.Forms.RichTextBox richTextBox_rules;
        private System.Windows.Forms.Button button_getrules;
        private System.Windows.Forms.ListView listView_showRules;
        private System.Windows.Forms.Button button_createDFA;
        private System.Windows.Forms.Button button_FillRuleTable;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Button button_All;
    }
}

